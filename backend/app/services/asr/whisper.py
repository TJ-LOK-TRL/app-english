import whisper
import numpy as np
from typing import Literal
from core.interfaces.iasr_service import IASRService, ASRResult, Segment

class WhisperASRService(IASRService):
    def __init__(
        self, 
        model_name: Literal['tiny', 'base', 'small', 'medium', 'large'] = 'base', 
        device: Literal['cuda', 'cpu'] = 'cuda'
    ) -> None:
        self.model = whisper.load_model(model_name, device)
        
    def transcribe(self, audio: np.ndarray) -> ASRResult:
        """Return transcribed text"""
        result = self.model.transcribe(audio, language='en', word_timestamps=True)
        
        segments = [
            Segment(segment_info['text'], segment_info['start'], segment_info['end'])
            for segment_info in result['segments']
        ]
        
        return ASRResult(result['text'], segments)