import pickle
from dataclasses import dataclass, asdict
from services.cache.diskcache_service import DiskCacheService
from core.interfaces.icache_service import CacheKey
from core.interfaces.iasr_service import ASRResult, Segment

@dataclass(frozen=True)
class ASRCacheKey(CacheKey):
    key: str
    lang: str 
    provider: str

class ASRCacheService(DiskCacheService[ASRCacheKey, ASRResult]):
    """Cache for ASR results (transcriptions and segments)"""
    
    def __init__(self, directory: str = 'asr_cache', size_limit_gb: int = 10) -> None:
        super().__init__(
            namespace='asr',
            directory=directory,
            size_limit=size_limit_gb * 1024 * 1024 * 1024,  # GB to bytes
            eviction_policy='least-recently-used',
            disk_min_file_size=4096,  # Audio optimization
            sqlite_cache_size=-1024 * 1024,  # 1GB SQLite cache
            sqlite_journal_mode='WAL'  # Write-ahead logging
        )
        self._hash_version = 'asr-v1'
    
    def _serialize_key(self, key: ASRCacheKey) -> str:
        # normalize key for consistent cache hits
        return key.to_cache_key(prefix='asr')
    
    def _serialize_value(self, value: ASRResult) -> bytes:
        # Use pickle to store full ASRResult (text + segments)
        # Convert Segment objects to dicts for serialization
        segments_list = [asdict(seg) for seg in value.segments]
        data = {'text': value.transcription, 'segments': segments_list}
        return pickle.dumps(data)
    
    def _deserialize_value(self, data: bytes) -> ASRResult:
        loaded = pickle.loads(data)
        segments = [Segment(**seg_dict) for seg_dict in loaded['segments']]
        return ASRResult(loaded['text'], segments)