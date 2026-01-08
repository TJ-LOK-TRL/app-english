import os
import torch
import traceback
from fastapi import APIRouter, UploadFile, File, Form, HTTPException
from fastapi.responses import JSONResponse, FileResponse
from core.enums.lang import Lang
from services.tts.kokoro import KokoroTTSService, KokoroVoice
from services.asr.whisper import WhisperASRService
from services.pronunciation.pronunciation_evaluator import PronunciationEvaluator
from services.chat.gemini import GeminiChatService
from services.tutor.interactive_chat_service import InteractiveChatService
from services.tutor.contextual_lesson_service import ContextualLessonService
from services.tutor.contextual_video_service import ContextualVideoService
from utils.audio_utils import wav_to_base64
from utils.api_utils import read_audio_file

router = APIRouter()

device = 'cuda' if torch.cuda.is_available() else 'cpu'
print('Using:', device)

# Services
tts_service = KokoroTTSService(device)
asr_service = WhisperASRService('tiny', device)
pronunciation_evaluator = PronunciationEvaluator(tts_service)
gemini_chat_service = GeminiChatService(os.getenv('GEMINI_API_KEY'))
interactive_chat_service = InteractiveChatService(gemini_chat_service)
lesson_service = ContextualLessonService(gemini_chat_service)
video_service = ContextualVideoService(gemini_chat_service, tts_service, os.getenv('FREEPIK_API_KEY'))

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
        # Read the uploaded audio file as bytes and decode it into a NumPy array
        audio_array = await read_audio_file(audio)

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

    # Convert WAV to base64
    audio_b64 = wav_to_base64(wav, sr)

    # Returns JSON with base64 audio, sr and tokens (timestamps + phonemes)
    return JSONResponse({
        'audio': audio_b64,
        'sample_rate': sr,
        'tokens': tokens_info,
        'pred_dur': pred_dur.tolist()
    })
    
@router.post('/converse')
async def converse(
    audio: UploadFile = File(...),
    lang: str = Form(Lang.EN_US),
    voice: str = Form(KokoroVoice.AMERICAN_FEMALE_HEART),
    speed: float = Form(1),
):
    """
    Receives audio, runs ASR, then TTS on recognized text.
    Returns JSON ready for TalkingHead:
    {
        "text": recognized text,
        "audio": base64 WAV,
        "tokens": [{"text":"word","start_ts":0.0,"end_ts":0.5}, ...],
        "pred_dur": durations for visemes
    }
    """
    # Read the uploaded audio file as bytes and decode it into a NumPy array
    audio_array = await read_audio_file(audio)

    # Run ASR
    recognized_text = asr_service.transcribe(audio_array).transcription
    print('Message is:', recognized_text)

    # Get a response
    response = interactive_chat_service.chat('0', recognized_text)
    print('Gemini response was:', response)

    # Run TTS on response
    wav, sr, tokens_info, pred_dur = tts_service.synthesize(
        text=response,
        lang=lang,
        speaker=voice,
        speed=speed
    )

    # Convert WAV to base64
    audio_b64 = wav_to_base64(wav, sr)
    
    print({
        'text': response,
        'tokens': tokens_info,
        'pred_dur': pred_dur.tolist()
    })
    
    # 5. Return JSON ready for TalkingHead
    return JSONResponse({
        'text': response,
        'audio': audio_b64,
        'tokens': tokens_info,
        'pred_dur': pred_dur.tolist()
    })
    
@router.post('/chat')
async def chat(
    text: str = Form(...),
    lang: str = Form(Lang.EN_US),
    voice: str = Form(KokoroVoice.AMERICAN_FEMALE_HEART),
    speed: float = Form(1),
):
    """
    Receives text input, gets AI response, optionally generates TTS.
    Returns JSON ready for TalkingHead (with or without audio).
    """
    # Get a response
    response = interactive_chat_service.chat('0', text)
    print('Gemini response was:', response)

    # Run TTS on response
    wav, sr, tokens_info, pred_dur = tts_service.synthesize(
        text=response,
        lang=lang,
        speaker=voice,
        speed=speed
    )

    # Convert WAV to base64
    audio_b64 = wav_to_base64(wav, sr)
    
    print({
        'text': response,
        'tokens': tokens_info,
        'pred_dur': pred_dur.tolist()
    })
    
    # 5. Return JSON ready
    return JSONResponse({
        'text': response,
        'audio': audio_b64,
        'tokens': tokens_info,
        'pred_dur': pred_dur.tolist()
    })
    
@router.post('/generate-lesson')
async def generate_lesson(context: str = Form(...)):
    """
    Generate a lesson based on context (GPS, Location, etc.)
    Returns: context_title and a list of items (original, translated, explanation)
    """
    try:
        print('Generate lesson...')
        # lesson_service already uses generate_parsed to return the Pydantic object
        lesson = lesson_service.generate_lesson(context)
        return lesson  # FastAPI automatically converts Pydantic to JSON :)
    except Exception as e:
        print('Error generating lesson:', str(e))
        traceback.print_exc()
        raise HTTPException(status_code=500, detail=str(e))
    
@router.post('/generate-video-content')
async def generate_video_content(user_input: str = Form(...)):
    """
    Generates the video script and image search keyword.
    16:9 output format is implicit in the short phrases.
    """
    try:
        print(f'Generating video content for: {user_input}')
        
        # Calls the AI chat service to generate video content
        video_content = video_service.generate_video_content(user_input)
        print(f'Video content:', video_content)
        
        # Create the video using tts, freepik and moviepy
        video_path = video_service.create_video_from_input(video_content)
        print(f'Video saved at:', video_path)
        
        if not os.path.exists(video_path):
            raise HTTPException(status_code=500, detail='Video file was not generated.')
        
        return FileResponse(
            path=video_path, 
            filename="generated_video.mp4", 
            media_type="video/mp4"
        )
    except Exception as e:
        print('Error generating video content:', str(e))
        traceback.print_exc()
        raise HTTPException(status_code=500, detail=str(e))
    