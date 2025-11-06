import pickle
from dataclasses import dataclass, asdict
from core.models.chat import Message, ChatHistory
from core.interfaces.icache_service import CacheKey
from services.cache.diskcache_service import DiskCacheService

@dataclass(frozen=True)
class ConversationCacheKey(CacheKey):
    user_id: str
    
    def to_cache_key(self) -> str:
        return self.user_id
    
class ConversationCacheService(DiskCacheService[ConversationCacheKey, ChatHistory]):
    def __init__(self, directory: str = 'conversation_cache', size_limit_gb: int = 5) -> None:
        super().__init__(
            namespace='conversation',
            directory=directory,
            size_limit=size_limit_gb * 1024 * 1024 * 1024,
            eviction_policy='least-recently-used'
        )
        self._hash_version = 'conv-v1'
    
    def _serialize_key(self, key: ConversationCacheKey) -> str:
        return key.to_cache_key()
    
    def _serialize_value(self, value: ChatHistory) -> bytes:
        data = {
            'user_id': value.user_id,
            'messages': [asdict(m) for m in value.messages]
        }
        return pickle.dumps(data)
    
    def _deserialize_value(self, data: bytes) -> ChatHistory:
        loaded = pickle.loads(data)
        messages = [Message(**m) for m in loaded['messages']]
        return ChatHistory(user_id=loaded['user_id'], messages=messages)