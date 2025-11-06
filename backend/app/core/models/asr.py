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