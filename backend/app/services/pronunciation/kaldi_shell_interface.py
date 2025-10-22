import os
import subprocess
from typing import List, Optional

class KaldiShellInterface:
    def __init__(self) -> None:
        self.kaldi_home = os.environ.get('KALDI_HOME', '')
        self.gop_home = os.path.join(self.kaldi_home, 'egs/gop_speechocean762')
    
    def _run_shell_script(self, script_name: str, args: List[str]) -> None:
        script_path = os.path.join(self.gop_home, script_name)
        full_command = ['/bin/bash', script_path] + args

        try:
            subprocess.run(full_command, check=True)
        except subprocess.CalledProcessError as e:
            print(f"Error running {script_name}:", e.stderr)
            raise

    def create_data_struct(self, text_file: str, wav_file: str, output_path: str, config_file: Optional[str] = None) -> None:
        args = [text_file, wav_file, output_path]
        if config_file:
            args.append(config_file)
        self._run_shell_script('local/create_struct.sh', args)

    def generate_reference_phones(self, text_file: str, wav_file: str, input_dir: str) -> None:
        self._run_shell_script('local/text-to-phone.sh', [text_file, wav_file, input_dir])

    def run_evaluator(self, text_file: str, wav_file: str, text_phone: str, input_dir: str) -> None:
        self._run_shell_script('run.sh', [text_file, wav_file, text_phone, input_dir])


