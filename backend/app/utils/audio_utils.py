import io
import base64
import numpy as np
import soundfile as sf

def bytes_to_audio_array(audio_bytes: bytes) -> np.ndarray:
    audio_array, _ = sf.read(io.BytesIO(audio_bytes))
    return np.array(audio_array, dtype=np.float32)

def wav_to_base64(wav: np.ndarray, sr: int) -> str:
    buffer = io.BytesIO()
    sf.write(buffer, wav, sr, format='WAV')
    buffer.seek(0)
    return base64.b64encode(buffer.read()).decode('utf-8')
