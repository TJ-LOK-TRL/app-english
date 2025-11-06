import numpy as np
from fastapi import UploadFile
from utils.audio_utils import bytes_to_audio_array

async def read_audio_file(upload: UploadFile) -> np.ndarray:
    """Reads an UploadFile and converts it to a NumPy audio array."""
    audio_bytes = await upload.read()
    return bytes_to_audio_array(audio_bytes)