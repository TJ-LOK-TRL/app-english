import numpy as np
from abc import ABC, abstractmethod
from typing import Optional, Tuple
from core.enums.lang import Lang

class ITTSService(ABC):
    @abstractmethod
    def tts(
        self, 
        text: str, 
        lang: Lang = Lang.EN_US,
        speaker: Optional[str] = None,
        speed: float = 1,
        **kargs
    ) -> Tuple[np.ndarray, int]:
        """Interface for Text-to-Speech services"""
    
    def tts_file(
        self, 
        output_path: str, 
        text: str, 
        lang: Lang = Lang.EN_US,
        speaker: Optional[str] = None,
        speed: float = 1,
        **kargs,
    ) -> None:
        """Convert text to speech and save to file"""
        wav, sr = self.tts(text, lang, speaker, speed, **kargs)
        
        import soundfile as sf
        sf.write(output_path, wav, sr)
  