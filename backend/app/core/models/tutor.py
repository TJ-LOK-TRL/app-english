from pydantic import BaseModel
from typing import List

class PhraseResponseStruct(BaseModel):
    response: str
    
class LessonItem(BaseModel):
    original: str
    translated: str
    explanation: str

class LessonResponseStruct(BaseModel):
    context_title: str
    items: List[LessonItem]