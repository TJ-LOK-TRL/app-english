import numpy as np
from typing import List, Tuple, Dict, Any, Literal
from core.enums.lang import Lang
from core.interfaces.itts_service import ITTSService
from services.tts.kokoro import KokoroVoice
from services.tts.cache import TTSCacheService, TTSCacheKey
from services.pronunciation.pronunciation_service import PronunciationService

def prepare_for_whisper(audio: np.ndarray, sr: int) -> np.ndarray:
    """
    Converts any input audio to Whisper-compatible format:
    - mono
    - float32 in [-1, 1]
    - 16 kHz sample rate
    """
    # Convert to mono
    if audio.ndim > 1:
        print('err/1')
        audio = np.mean(audio, axis=1)
    
    # Convert to float32
    audio = audio.astype(np.float32)

    # Normalize to [-1, 1]
    max_abs = np.max(np.abs(audio))
    if max_abs > 0:
        print('err/2')
        audio = audio / max_abs

    # Resample to 16kHz if needed
    if sr != 16000:
        print('err/3')
        import librosa
        audio = librosa.resample(audio, orig_sr=sr, target_sr=16000)

    return audio.astype(np.float32)

class PronunciationEvaluator:
    """Orchestrates pronunciation evaluation using multiple services"""
    
    def __init__(self, tts_service: ITTSService) -> None:        
        self._tts_service = tts_service
        self._tts_cache = TTSCacheService()
        
        self.pronunciation_service = PronunciationService()
        
        self._default_ref_audio_params = {
            'speed': 1.0,
            'lang': Lang.EN_US,
            'speaker': KokoroVoice.AMERICAN_FEMALE_HEART,
            'sample_rate': 24000
        }
    
    def evaluate(self, usr_audio: np.ndarray, target_text: str) -> Dict[str, Any]: # TODO use dataclass in return
        """Main method to evaluate pronunciation"""
                                
        # 1. Get reference audio cache key
        tts_cache_key = TTSCacheKey(target_text, provider='kokoro', **self._default_ref_audio_params)
                                
        # 2. Get reference audio (with caching)
        cached_audio = self._tts_cache.get(tts_cache_key)
        if cached_audio is not None:
            print('Getting reference audio for text from cache')
            ref_audio, sr = cached_audio
        else:
            print('Getting reference audio for text:', target_text)
            ref_audio, sr = self._tts_service.tts(target_text, **self._default_ref_audio_params)
            # Cache the result
            self._tts_cache.set(tts_cache_key, (ref_audio, sr))
                    
        ref_audio = prepare_for_whisper(ref_audio, sr)
                    
        scores = self.pronunciation_service.run_pipeline(tts_cache_key.to_cache_key(), target_text, ref_audio, usr_audio)
        
        results: List[List[Tuple[str, float]]] = self.evaluate_pronunciation_per_word(scores)
        print('Final result:', results)
        return {
            'results': results
        }
        
    def evaluate_pronunciation_per_word(
        self,
        aligned: List[List[Tuple[str, float]]]
    ) -> List[Dict[str, Any]]:
        """
        For each word (list of phonemes with scores), compute:
        - word text (as string of phonemes)
        - average score
        - label (passed / average / failed)
        """
        results = []
        for word in aligned:
            phonemes = [ph for ph, _ in word]
            scores = [score for _, score in word]
            avg_score = sum(scores) / len(scores) if scores else 0.0
            label = self.score_to_label(avg_score)
            results.append({
                'phonemes': phonemes,
                'score': round(avg_score, 4),
                'label': label
            })
        return results
    
    def score_to_label(self, score: float) -> Literal['passed', 'average', 'failed']:
        """
        Maps a score to a qualitative label.
        You can tweak the thresholds as needed.
        """
        if score >= -0.3:
            return 'passed'
        elif score >= -0.7:
            return 'average'
        else:
            return 'failed'