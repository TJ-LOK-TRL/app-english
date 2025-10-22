import re
import os
import subprocess
from typing import List, Tuple, Optional

class KaldiShellInterface:
    def __init__(self) -> None:
        self.kaldi_home = os.environ.get('KALDI_HOME', '')
        self.gop_home = os.path.join(self.kaldi_home, 'egs/gop_speechocean762/s5')
        self.data_home = os.path.join(self.gop_home, 'data')
    
    def _run_shell_script(self, script_path: str, args: List[str], cwd: Optional[str] = None) -> str:
        full_command = ['/bin/bash', script_path] + args

        try:
            result = subprocess.run(
                full_command,
                check=True,
                capture_output=True,
                cwd=cwd,
                text=True
            )
            return result.stdout
        except subprocess.CalledProcessError as e:
            print(f'Error running {script_path}:', e.stderr)
            raise

    def generate_reference_phones(self, text_file: str, wav_file: str, input_dir: str) -> str:
        return self._run_shell_script(
            os.path.join(self.gop_home, 'local/text-to-phone.sh'), 
            [text_file, wav_file, input_dir], 
            cwd=self.gop_home
        )

    def run_evaluator(self, text_file: str, wav_file: str, text_phone: str, input_dir: str) -> str:
        return self._run_shell_script(
            'services/pronunciation/run.sh', 
            [text_file, wav_file, text_phone, input_dir]
        )

    def format_result(self, gop_result: str) -> Tuple[str, List[Tuple[str, float]]]:
        """Processes GOP (Goodness of Pronunciation) data from Kaldi files and returns results as a list."""
        
        phones_file = os.path.join(self.data_home, 'lang_nosp/phones-pure.txt')

        # Load mapping: index -> phoneme
        phone_map = {}
        with open(phones_file, 'r') as f:
            for line in f:
                parts = line.strip().split()
                if len(parts) == 2:
                    phone, idx = parts
                    phone_map[int(idx)] = phone

        # Regex to capture each [index value]
        pattern = re.compile(r'\[\s*(\d+)\s*([-\d.e]+)\s*\]')

        line = gop_result.strip()
        utt = line.split()[0]  # UTT1
        gop_list = []
        for match in pattern.finditer(line):
            idx, gop = match.groups()
            idx = int(idx)
            gop_list.append((phone_map.get(idx, f'UNK{idx}'), float(gop)))
        
        return (utt, gop_list)
        
         
