from typing import List
from dataclasses import dataclass

@dataclass
class Message:
    role: str
    text: str

@dataclass
class ChatHistory:
    user_id: str
    messages: List[Message]
    
    def add_message(self, role: str, text: str) -> None:
        self.messages.append(Message(role, text))