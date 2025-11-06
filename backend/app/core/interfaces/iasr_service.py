import numpy as np
from abc import ABC, abstractmethod
from core.models.asr import ASRResult

class IASRService(ABC):
    """Interface for all ASR service implementations"""
    
    @abstractmethod
    def __init__(self, model_name: str, **kwargs) -> None:
        """Initialize ASR model with specific model version"""
    
    @abstractmethod
    def transcribe(self, audio: np.ndarray) -> ASRResult:
        """Process audio and return transcription with metadata"""