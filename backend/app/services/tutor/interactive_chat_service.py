from core.models.chat import ChatHistory
from core.models.tutor import PhraseResponseStruct
from core.interfaces.ichat_service import IChatService
from services.chat.cache import ConversationCacheService, ConversationCacheKey

class InteractiveChatService:
    def __init__(self, chat_service: IChatService) -> None:
        self.chat_service = chat_service
        self.cache = ConversationCacheService()
        self.system_message = (
            'You are an English learning assistant. '
            'Your goal is to keep a natural, friendly conversation in English '
            'with the user. If the user makes a grammar or vocabulary mistake, '
            "respond with: 'Did you mean: ...' and provide the corrected version, "
            'without interrupting the flow of the conversation. '
            'Keep your responses short and simple, suitable for learners.'
        )
        
    def chat(self, user_id: str, message: str) -> str:
        key = ConversationCacheKey(user_id)
        
        # Load or create the chat history
        chat_history = self.cache.get(key) or ChatHistory(user_id, [])
        
        # Cut for testing
        # TODO: Remove this part in the future
        if len(chat_history.messages) > 4:
            chat_history.messages = chat_history.messages[-4:]
        
        # Call chat api
        response = self.chat_service.generate_parsed(message, PhraseResponseStruct, chat_history, self.system_message).response
        
        # Update conversation history
        chat_history.add_message('user', message)
        chat_history.add_message('model', response)
        self.cache.set(key, chat_history)
        
        return response
        
        
        
    
        
