import pickle
import numpy as np
from typing import Tuple, Self
from dataclasses import dataclass
from services.cache.diskcache_service import DiskCacheService
from core.interfaces.icache_service import CacheKey

@dataclass(frozen=True)
class TTSCacheKey(CacheKey):
    """Immutable parameters for TTS generation"""
    text: str
    speed: float
    lang: str
    speaker: str
    sample_rate: int
    provider: str
    
    def normalized(self) -> Self:
        """Return normalized version for consistent caching"""
        return TTSCacheKey(
            text=str(self.text).strip().lower(),
            speed=self.speed,
            lang=str(self.lang).strip().lower(),
            speaker=str(self.speaker).strip().lower(),
            sample_rate=self.sample_rate,
            provider=str(self.provider).strip()
        )
        
class TTSCacheService(DiskCacheService[TTSCacheKey, Tuple[np.ndarray, int]]):
    """Specialized cache service for TTS audio"""
    
    def __init__(self, directory: str = 'tts_cache', size_limit_gb: int = 10) -> None:
        super().__init__(
            namespace='tts',
            directory=directory,
            size_limit=size_limit_gb * 1024 * 1024 * 1024,  # GB to bytes
            eviction_policy='least-recently-used',
            disk_min_file_size=4096,  # Audio optimization
            sqlite_cache_size=-1024 * 1024,  # 1GB SQLite cache
            sqlite_journal_mode='WAL'  # Write-ahead logging
        )
        self._hash_version = 'tts-v1'
    
    def _serialize_key(self, key: TTSCacheKey) -> str:
        """Generate deterministic cache key from TTSCacheKey"""
        return key.to_cache_key(prefix='tts')
    
    def _serialize_value(self, value: Tuple[np.ndarray, int]) -> bytes:
        """Serialize audio data and sample rate"""
        audio, sr = value
        
        assert isinstance(audio, np.ndarray), 'audio must be a numpy array'
        assert isinstance(sr, int), 'sample_rate must be an integer'
        
        return pickle.dumps({'audio': audio, 'sample_rate': sr})
    
    def _deserialize_value(self, data: bytes) -> Tuple[np.ndarray, int]:
        """Deserialize audio data and sample rate"""
        loaded = pickle.loads(data)
        return loaded['audio'], loaded['sample_rate']
