import numpy as np
from abc import ABC, abstractmethod
from typing import List
from dataclasses import dataclass

@dataclass
class Segment:
    text: str
    start_t: float
    end_t: float

@dataclass
class ASRResult:
    transcription: str
    segments: List[Segment]

class IASRService(ABC):
    """Interface for all ASR service implementations"""
    
    @abstractmethod
    def __init__(self, model_name: str, **kwargs) -> None:
        """Initialize ASR model with specific model version"""
    
    @abstractmethod
    def transcribe(self, audio: np.ndarray) -> ASRResult:
        """Process audio and return transcription with metadata"""