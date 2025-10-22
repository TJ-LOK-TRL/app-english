import os
import subprocess
import numpy as np
import soundfile as sf
from services.pronunciation.kaldi_shell_interface import KaldiShellInterface

class PronunciationService:
    def __init__(self) -> None:
        self.data_home = '/usr/src/data'
        self.ksi = KaldiShellInterface()

    def run_pipeline(self, text: str, ref_wav: np.ndarray, usr_wav: np.ndarray) -> None:
        input_dir = hash(text) 

        text_file = os.path.join(self.data_home, input_dir, 'text.txt')
        usr_wav_file = os.path.join(self.data_home, input_dir, 'usr_wav.wav')
        ref_wav_file = os.path.join(self.data_home, input_dir, 'ref_wav.wav')

        with open(text_file, 'tw') as file:
            file.write(text)

        sf.write('usr_wav.wav', usr_wav, 16000)
        sf.write('ref_wav.wav', ref_wav, 24000)

        self.ksi.create_data_struct(text_file, usr_wav_file, input_dir, )

    
    
