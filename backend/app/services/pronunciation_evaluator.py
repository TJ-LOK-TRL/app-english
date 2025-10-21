import torch
import numpy as np
from typing import Dict, Any
from core.enums.lang import Lang
from services.asr.whisper import WhisperASRService
from services.tts.kokoro import KokoroTTSService, KokoroVoice
from services.asr.cache import ASRCacheService, ASRCacheKey
from services.tts.cache import TTSCacheService, TTSCacheKey

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
    
    def __init__(self) -> None:
        device = 'cuda' if torch.cuda.is_available() else 'cpu'
        print('Using:', device)
        
        self._asr_service = WhisperASRService('small', device)
        self._tts_service = KokoroTTSService(device)
        self._asr_cache = ASRCacheService()
        self._tts_cache = TTSCacheService()
        
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
            
        import soundfile as sf
        sf.write('ref_audo_test.wav', ref_audio, sr)
        sf.write('usr_audo_test.wav', usr_audio, 16000)
        
        ref_audio = prepare_for_whisper(ref_audio, sr)
            
        # 3. Transcribe reference (with caching)
        asr_cache_key = ASRCacheKey(key=tts_cache_key.to_hash(), provider='whisper-small', lang='en')
        
        cached_transcription = self._asr_cache.get(asr_cache_key)
        if cached_transcription is not None:
            print('Getting reference transcription for audio from cache')
            ref_transcript = cached_transcription
        else:
            print('Getting reference transcription for audio with text:', target_text)
            # Cache the result
            ref_transcript = self._asr_service.transcribe(ref_audio)
            self._asr_cache.set(asr_cache_key, ref_transcript)
            
        
        print('Getting transcriptions')
        usr_transcript = self._asr_service.transcribe(usr_audio)
        
        # Debug
        print(usr_transcript)
        print(ref_transcript)
        
        # TODO
        return {
            'score': 0.0,
            'transcript': ref_transcript.transcription,
            'feedback': ''
        }
        