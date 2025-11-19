from google import genai 
from google.genai import types
from typing import Dict, Optional, Any, Literal, Type
from core.models.chat import ChatHistory
from core.interfaces.ichat_service import IChatService, T

class GeminiChatService(IChatService):
    """
    Service wrapper for Gemini chat-based interactions with conversation history support.
    """
    
    def __init__(self, api_key: str, model: str = 'gemini-2.0-flash') -> None:
        self.model = model
        self.client = genai.Client(api_key=api_key)
        
    def _get_content(self, role: Literal['user', 'model', 'system'], text: str) -> types.Content:
        """Helper to build a Gemini-compatible content object."""
        return types.Content(role=role, parts=[types.Part.from_text(text=text)])
         
    def _generate(
        self, 
        prompt: str, 
        history: Optional[ChatHistory] = None, 
        system_message: Optional[str] = None, 
        config: Optional[Dict[str, Any]] = None
    ) -> types.GenerateContentResponse:
        """
        Generates a model response given the user prompt, optional history, and optional system message.

        Args:
            prompt: User input to generate a response for.
            history: Optional chat history (previous messages).
            system_message: Optional system-level instruction (sets behavior/tone).
            config: Optional model generation parameters (e.g., temperature, max_output_tokens).

        Returns:
            str: The generated response text.
        """
        contents = []
        
        # Add optional system message
        if system_message:
            # System role not supported by gemini
            #contents.append(self._get_content('system', system_message))
            
            # Try simulate system role
            contents.append(self._get_content('user', system_message))
            contents.append(self._get_content('model', 'Understood'))
        
        # Add previous chat history
        if history and history.messages:
            contents = [self._get_content(message.role, message.text) for message in history.messages]
        
        # Add current prompt message
        contents.append(self._get_content('user', prompt))
                            
        # Call Gemini API          
        return self.client.models.generate_content(
            model=self.model,
            contents=contents,
            config=config
        )
        
    def generate(
        self, 
        prompt: str, 
        history: Optional[ChatHistory] = None, 
        system_message: Optional[str] = None, 
        *,
        config: Optional[Dict[str, Any]] = None
    ) -> str:
        return self._generate(prompt, history, system_message, config).text
    
    def generate_parsed(
        self, 
        prompt: str, 
        response_schema: Type[T] = None,
        history: Optional[ChatHistory] = None, 
        system_message: Optional[str] = None, 
        *,
        config: Optional[Dict[str, Any]] = None
    ) -> T:
        # Add response schema
        if response_schema:
            config = config or {}
            config |= {
                'response_mime_type': 'application/json',
                'response_schema': response_schema
            }
        return self._generate(prompt, history, system_message, config).parsed