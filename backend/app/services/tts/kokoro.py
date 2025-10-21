import numpy as np
from typing import Tuple, Dict
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
        Convert text to speech using Kokoro TTS
        
        Args:
            text: Input text to synthesize
            lang: Language of the text
            speaker: Voice to use for synthesis
            speed: Speech speed multiplier
            sample_rate: Output sample rate
            
        Returns:
            Tuple of (audio_array, sample_rate)
            
        Raises:
            ValueError: If language is not supported
        """
        
        # Convert BCP47 to Kokoro language code
        kokoro_lang = self.BCP47_TO_KOKORO.get(lang)
        if kokoro_lang is None:
            raise ValueError(f'Kokoro does not support language: {lang}')
        
        # Load pipeline for language
        pipeline = self.load_model(kokoro_lang)
        
        # Generate audio
        generator = pipeline(text, voice=speaker, speed=speed)
        
        # Concatenate all audio chunks
        full_audio = np.concatenate([audio for _, _, audio in generator])
        return full_audio, sample_rate
    