import json
import hashlib
from dataclasses import dataclass, asdict
from abc import ABC, abstractmethod
from typing import Generic, TypeVar, Optional, Self

# Type variables for flexibility
T_Key = TypeVar('T_Key')
T_Value = TypeVar('T_Value')

@dataclass(frozen=True)
class CacheKey:
    """Base class for deterministic cache keys"""
    
    def normalized(self) -> Self:
        """Override in subclasses if normalization is needed"""
        return self

    def to_hash(self, version: str = 'v1', digest_size: int = 16) -> str:
        """Generate deterministic string hash for cache"""
        normalized_key = self.normalized()
        key_dict = asdict(normalized_key)
        
        # Convert enums and other special types to serializable values
        serializable_dict = self._make_serializable(key_dict)
        
        hash_input = {'version': version, 'params': serializable_dict}
        param_str = json.dumps(hash_input, sort_keys=True, separators=(',', ':'))
        return hashlib.blake2b(param_str.encode(), digest_size=digest_size).hexdigest()
    
    def _make_serializable(self, data: dict) -> dict:
        """Convert non-serializable values to strings"""
        serializable = {}
        for key, value in data.items():
            if hasattr(value, 'value'):  # Handle enums
                serializable[key] = value.value
            else:
                serializable[key] = value
        return serializable

    def to_cache_key(self, prefix: str = 'cache') -> str:
        """Generate full cache key with prefix"""
        return f'{prefix}_{self.to_hash()}'

@dataclass
class CacheStats:
    """Cache statistics data structure"""
    hits: int
    misses: int
    hit_ratio: float
    volume: int
    size_mb: float
    
class ICacheService(ABC, Generic[T_Key, T_Value]):
    """Base interface for all cache services"""
    
    @abstractmethod
    def get(self, key: T_Key) -> Optional[T_Value]:
        """Retrieve value from cache"""
        pass
    
    @abstractmethod
    def set(self, key: T_Key, value: T_Value) -> None:
        """Store value in cache"""
        pass
    
    @abstractmethod
    def get_stats(self) -> CacheStats:
        """Get cache performance statistics"""
        pass
    
    @abstractmethod
    def clear_expired(self) -> None:
        """Clean up expired entries"""
        pass
