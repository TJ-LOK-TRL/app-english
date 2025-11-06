from typing import Optional
from abc import ABC, abstractmethod
from core.models.chat import ChatHistory

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