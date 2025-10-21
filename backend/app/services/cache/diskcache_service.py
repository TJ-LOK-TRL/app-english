import os
from abc import abstractmethod
from diskcache import Cache
from typing import Generic, Optional
from core.interfaces.icache_service import ICacheService, CacheStats, T_Key, T_Value

class DiskCacheService(ICacheService[T_Key, T_Value], Generic[T_Key, T_Value]):
    """Base disk cache implementation using diskcache"""
    
    def __init__(
        self, 
        namespace: str,
        directory: str,
        **cache_kwargs
    ) -> None:
        self._namespace = namespace
        self._directory = os.path.join('cache', directory, namespace)
        
        self._cache = Cache(
            directory=self._directory,
            **cache_kwargs
        )
    
    def get(self, key: T_Key) -> Optional[T_Value]:
        """Retrieve value from cache"""
        cache_key = self._serialize_key(key)
        
        with self._cache as cache:
            result = cache.get(cache_key)
            
        if result is not None:
            self._on_cache_hit()
            return self._deserialize_value(result)
        
        self._on_cache_miss()
        return None
    
    def set(self, key: T_Key, value: T_Value) -> None:
        """Store value in cache"""
        cache_key = self._serialize_key(key)
        serialized_value = self._serialize_value(value)
        
        with self._cache as cache:
            cache.set(cache_key, serialized_value, expire=None)
        
        print(f'[{self._namespace}] Cached with key: {cache_key[:16]}...')
    
    def get_stats(self) -> CacheStats:
        """Get cache performance statistics"""
        hits, misses = self._cache.stats()
        total = hits + misses
        hit_ratio = (hits / total) * 100 if total > 0 else 0
        
        return CacheStats(
            hits=hits,
            misses=misses,
            hit_ratio=hit_ratio,
            volume=self._cache.volume(),
            size_mb=self._get_directory_size()
        )
    
    def clear_expired(self) -> None:
        """Clean up expired entries"""
        self._cache.expire()
    
    def _get_directory_size(self) -> float:
        """Calculate total size of cache directory in MB"""
        total_size = 0
        for dirpath, dirnames, filenames in os.walk(self._directory):
            for filename in filenames:
                filepath = os.path.join(dirpath, filename)
                total_size += os.path.getsize(filepath)
        return total_size / (1024 * 1024)
    
    def _on_cache_hit(self) -> None:
        """Override for cache hit logging"""
        if self._cache.stats()[0] % 100 == 0:
            self._log_cache_stats()
    
    def _on_cache_miss(self) -> None:
        """Override for cache miss logging"""
        pass
    
    def _log_cache_stats(self) -> None:
        """Log cache performance metrics"""
        stats = self.get_stats()
        print(f'[{self._namespace}] Cache Stats - Hits: {stats.hits}, Misses: {stats.misses}, Ratio: {stats.hit_ratio:.1f}%')
    
    # Abstract methods for serialization (should be implemented by subclasses)
    @abstractmethod
    def _serialize_key(self, key: T_Key) -> str:
        """Convert key to string for caching"""
        pass
    
    @abstractmethod
    def _serialize_value(self, value: T_Value) -> bytes:
        """Serialize value for storage"""
        pass
    
    @abstractmethod
    def _deserialize_value(self, data: bytes) -> T_Value:
        """Deserialize value from storage"""
        pass