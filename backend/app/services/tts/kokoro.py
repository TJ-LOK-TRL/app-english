import numpy as np
from torch import LongTensor
from typing import List, Tuple, Dict, Any
from kokoro import KPipeline
from enum import Enum
from core.enums.lang import Lang
from core.interfaces.itts_service import ITTSService

class KokoroVoice(str, Enum):
    """Available voices in Kokoro TTS"""
    
    # Portuguese Voices
    PORTUGUESE_FEMALE_DORA = 'pf_dora'
    PORTUGUESE_MALE_ALEX = 'pm_alex'
    PORTUGUESE_MALE_SANTA = 'pm_santa'
    
    # American English Voices
    AMERICAN_FEMALE_HEART = 'af_heart'
    AMERICAN_FEMALE_BELLA = 'af_bella'
    AMERICAN_FEMALE_NICOLE = 'af_nicole'
    AMERICAN_MALE_MICHAEL = 'am_michael'
    
    # British English Voices
    BRITISH_FEMALE_EMMA = 'bf_emma'
    BRITISH_MALE_GEORGE = 'bm_george'

class KokoroLang(str, Enum):
    """Kokoro-specific language codes"""
    ENGLISH = 'a'
    PORTUGUESE = 'p'

class KokoroTTSService(ITTSService):
    """
    Kokoro TTS service implementation
    
    Language codes mapping:
    'a' => American English
    'b' => British English  
    'e' => Spanish
    'f' => French
    'h' => Hindi
    'i' => Italian
    'j' => Japanese
    'p' => Brazilian Portuguese
    'z' => Mandarin Chinese
    """
    
    # BCP47 to Kokoro language codes mapping
    BCP47_TO_KOKORO = {
        Lang.EN_US: KokoroLang.ENGLISH,
        Lang.EN_GB: KokoroLang.ENGLISH,
        Lang.PT_BR: KokoroLang.PORTUGUESE,
        Lang.PT_PT: KokoroLang.PORTUGUESE,
    }
    
    def __init__(self, device: str = 'cuda') -> None:
        """Initialize Kokoro TTS service
        
        Args:
            device: Device to run model on ('cuda' or 'cpu')
        """
        self.device = device
        self.cache: Dict[str, KPipeline] = {}
        
    def load_model(self, lang: str) -> KPipeline: 
        """Load or get cached Kokoro pipeline for specific language"""
        if lang not in self.cache:
            model = KPipeline(lang_code=lang, device=self.device)
            self.cache[lang] = model
        return self.cache[lang]

    def tts(
        self, 
        text: str, 
        lang: Lang = Lang.EN_US,
        speaker: str = KokoroVoice.AMERICAN_FEMALE_HEART,
        speed: float = 1.0,
        *,
        sample_rate: int = 24000,
    ) -> Tuple[np.ndarray, int]:
        """
        Simple TTS call: returns only audio and sample rate, using `synthesize` internally.
        """
        full_audio, sr, *_ = self.synthesize(
            text=text,
            lang=lang,
            speaker=speaker,
            speed=speed,
            sample_rate=sample_rate
        )
        return full_audio, sr
    
    def synthesize(
        self, 
        text: str, 
        lang: Lang = Lang.EN_US,
        speaker: str = KokoroVoice.AMERICAN_FEMALE_HEART,
        speed: float = 1.0,
        *,
        sample_rate: int = 24000,
    ) -> Tuple[np.ndarray, int, List[Dict[str, Any]], LongTensor]:
        """
        Synthesize text to audio with Kokoro TTS, returning timestamps and phonemes.

        Returns:
            Tuple containing:
                - full_audio: np.ndarray, concatenated audio
                - sample_rate: int, sample rate
                - tokens_info: List[Dict], each dict contains:
                    'text', 'start_ts', 'end_ts', 'phonemes'
        """
        # Convert BCP47 to Kokoro language code
        kokoro_lang = self.BCP47_TO_KOKORO.get(lang)
        if kokoro_lang is None:
            raise ValueError(f'Kokoro does not support language: {lang}')
        
        # Load pipeline for language
        pipeline = self.load_model(kokoro_lang)
        
        # Generate audio chunks
        generator = pipeline(text, voice=speaker, speed=speed)
        
        all_audio_chunks = []
        tokens_info = []

        for result in generator:
            # Append audio
            all_audio_chunks.append(result.audio.cpu().numpy())
            
            # Extract tokens with timestamps and phonemes
            for t in result.tokens:
                tokens_info.append({
                    'text': t.text + (' ' if t.whitespace else ''),
                    'whitespace': t.whitespace,
                    'start_ts': t.start_ts,
                    'end_ts': t.end_ts,
                    'phonemes': t.phonemes
                })
        
        # Concatenate all audio chunks
        full_audio = np.concatenate(all_audio_chunks)
        
        return full_audio, sample_rate, tokens_info, result.pred_dur
        

    