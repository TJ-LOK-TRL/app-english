import os
import numpy as np
import soundfile as sf
from typing import List, Tuple
from services.pronunciation.kaldi_shell_interface import KaldiShellInterface
from utils.file_utils import create_tmp_dir, remove_dir, get_next_subdir

class PronunciationService:
    def __init__(self) -> None:
        self.data_home = '/usr/src/data'
        os.makedirs(self.data_home, exist_ok=True)
        
        self.ksi = KaldiShellInterface()

    def run_pipeline(self, id: str, text: str, ref_wav: np.ndarray, usr_wav: np.ndarray) -> List[Tuple[str, float]]:
        input_dir = os.path.join(self.data_home, id)
        
        usr_input_dir = get_next_subdir(input_dir, 'usr_')
        tmp_dir = os.path.join(self.data_home, create_tmp_dir(prefix=f'user_{id}'))
        text_file = os.path.join(tmp_dir, 'text.txt')
        usr_wav_file = os.path.join(tmp_dir, 'usr_wav.wav')
        ref_wav_file = os.path.join(tmp_dir, 'ref_wav.wav')

        try:
            with open(text_file, 'tw') as file:
                file.write(text)

            sf.write(usr_wav_file, usr_wav, 16000)
            sf.write(ref_wav_file, ref_wav, 24000)

            self.ksi.generate_reference_phones(text_file, ref_wav_file, input_dir)
            ref_phones = os.path.join(input_dir, 'text-phone')
            
            print('yyy:', ref_phones)
            result = self.ksi.run_evaluator(text_file, usr_wav_file, ref_phones, usr_input_dir)
            print('Raw GOP result:', result)
            
            _, scores = self.ksi.format_result(result)
            print('Scores:', scores)
            
            return scores
        finally:
            remove_dir(tmp_dir)


    
    
