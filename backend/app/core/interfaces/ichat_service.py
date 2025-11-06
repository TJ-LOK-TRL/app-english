from typing import Optional, TypeVar, Type
from abc import ABC, abstractmethod
from pydantic import BaseModel
from core.models.chat import ChatHistory

T = TypeVar('T', bound=BaseModel)

class IChatService(ABC):
    @abstractmethod
    def generate(
        self, 
        prompt: str, 
        history: Optional[ChatHistory] = None, 
        system_message: Optional[str] = None,
        **kwargs
    ) -> str:
        """Generate a model response with optional chat history and system message."""
        
    @abstractmethod
    def generate_parsed(
        self, 
        prompt: str, 
        response_schema: Type[T] = None,
        history: Optional[ChatHistory] = None, 
        system_message: Optional[str] = None,
        **kwargs
    ) -> T:
        """Generate a structured response parsed into the provided Pydantic model."""