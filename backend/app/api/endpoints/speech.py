import io
import torch
import base64
import traceback
import numpy as np
import soundfile as sf
from fastapi import APIRouter, UploadFile, File, Form, HTTPException
from fastapi.responses import JSONResponse
from core.enums.lang import Lang
from services.tts.kokoro import KokoroTTSService, KokoroVoice
from services.pronunciation.pronunciation_evaluator import PronunciationEvaluator

router = APIRouter()

device = 'cuda' if torch.cuda.is_available() else 'cpu'
print('Using:', device)

tts_service = KokoroTTSService(device)
pronunciation_evaluator = PronunciationEvaluator(tts_service)

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
        audio_array, _ = sf.read(io.BytesIO(audio_bytes))
        audio_array = np.array(audio_array, dtype=np.float32)
        print('Audio array:', audio_array)

        # Evaluate pronunciation using your evaluator
        result = pronunciation_evaluator.evaluate(audio_array, target_text)
        return result
    except Exception as e:
        print('Exception occurred:', str(e))
        traceback.print_exc()
        raise HTTPException(status_code=500, detail=str(e))
    
@router.post('/kokoro/synthesize')
async def synthesize(
    text: str = Form(...),
    lang: str = Form(Lang.EN_US),
    voice: str = Form(KokoroVoice.AMERICAN_FEMALE_HEART),
    speed: float = Form(1)
):
    # Calls synthesize which returns audio + sr + tokens_info
    wav, sr, tokens_info, pred_dur = tts_service.synthesize(
        text=text,
        lang=lang,
        speaker=voice,
        speed=speed
    )

    # Writes audio to WAV buffer
    buffer = io.BytesIO()
    sf.write(buffer, wav, sr, format='WAV')
    buffer.seek(0)

    # Converts audio to base64
    audio_b64 = base64.b64encode(buffer.read()).decode('utf-8')

    # Returns JSON with base64 audio, sr and tokens (timestamps + phonemes)
    return JSONResponse({
        'audio': audio_b64,
        'sample_rate': sr,
        'tokens': tokens_info,
        'pred_dur': pred_dur.tolist()
    })