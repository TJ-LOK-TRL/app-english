import io
import traceback
import numpy as np
import soundfile as sf
from fastapi import APIRouter, UploadFile, File, Form, HTTPException
from services.pronunciation_evaluator import PronunciationEvaluator

router = APIRouter()
pronunciation_evaluator = PronunciationEvaluator()

@router.post('/evaluate-pronunciation')
async def pronunciation_check(audio: UploadFile = File(...), target_text: str = Form(...)):
    '''
    Endpoint to check pronunciation accuracy.
    Args:
        file: Audio file uploaded by the client
        expected_text: The target phrase to compare against
    Returns:
        JSON response with recognized text and accuracy score
    '''
    try:
        # Read the uploaded audio file as bytes
        audio_bytes = await audio.read()
        print('Audio bytes:', len(audio_bytes))

        # Decode the audio bytes into a NumPy array
        audio_array, sample_rate = sf.read(io.BytesIO(audio_bytes))
        audio_array = np.array(audio_array, dtype=np.float32)
        print('Audio array:', audio_array)

        # Evaluate pronunciation using your evaluator
        result = pronunciation_evaluator.evaluate(audio_array, target_text)
        return result
    except Exception as e:
        print('Exception occurred:', str(e))
        traceback.print_exc()
        raise HTTPException(status_code=500, detail=str(e))