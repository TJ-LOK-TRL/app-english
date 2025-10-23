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

    def format_result(self, gop_result_raw: str, ref_phones_raw: str) -> List[List[Tuple[str, float]]]:
        _, gop_result = self.format_gop_result(gop_result_raw)
        ref_phones = self.format_phonemes(ref_phones_raw)

        print(gop_result)
        print(ref_phones)

        return self.align_phonemes_with_scores(gop_result, ref_phones)

    def format_gop_result(self, gop_result: str) -> Tuple[str, List[Tuple[str, float]]]:
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
    
    def format_phonemes(self, data: str) -> List[Tuple[str]]:
        """
        Extracts phonemes - each line is a word
        """
        words = []
        
        lines = data.strip().split('\n')
        
        for line in lines:
            parts = line.split()
            # Remove the first element (utt1.0, utt1.1, etc)
            phonemes = parts[1:]
            words.append(tuple(self.clear_phones(phonemes)))
        
        return words
    
    def clear_phones(self, phonemes: List[str]) -> List[str]:
        """
        Removes suffixes (_B, _I, _E, _S) and digits from phonemes like 'AH0' → 'AH'
        """
        cleaned = []
        for phone in phonemes:
            # Remove suffixes like _B, _I, _E, _S
            phone = re.sub(r'_[BIES]$', '', phone)
            # Remove digits (like 0, 1, 2) from the phoneme (e.g., AH0 → AH)
            phone = re.sub(r'\d', '', phone)
            cleaned.append(phone)
        return cleaned

    def align_phonemes_with_scores(
        self,
        scored_phones: List[Tuple[str, float]],
        segmented_words: List[Tuple[str]]
    ) -> List[List[Tuple[str, float]]]:
        """
        Aligns scored phonemes with their corresponding word segments.
        """
        aligned = []
        index = 0

        for word in segmented_words:
            word_alignment = []
            for phoneme in word:
                if index >= len(scored_phones):
                    raise ValueError('Mismatch between scored phonemes and segments.')
                scored_phoneme, score = scored_phones[index]
                if scored_phoneme != phoneme:
                    raise ValueError(
                        f'Phoneme mismatch at index {index}: expected \'{phoneme}\', got \'{scored_phoneme}\''
                    )
                word_alignment.append((scored_phoneme, score))
                index += 1
            aligned.append(word_alignment)

        return aligned


        
         
