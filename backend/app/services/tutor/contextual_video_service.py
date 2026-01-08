# -*- coding: utf-8 -*-
import os
import uuid
import requests
import numpy as np
from typing import Tuple
from moviepy.video.fx import FadeIn, FadeOut
from deep_translator import GoogleTranslator
from moviepy import TextClip, ImageClip, CompositeVideoClip, concatenate_videoclips, concatenate_audioclips, AudioFileClip, AudioClip
from pathlib import Path
from core.interfaces.ichat_service import IChatService
from core.interfaces.itts_service import ITTSService
from core.models.tutor import VideoResponseStruct
from services.image.freepik import generate_image_info, wait_for_image

class ContextualVideoService:
    def __init__(self, chat_service: IChatService, tts_service: ITTSService, freepik_api: str) -> None:
        self.freepik_api = freepik_api
        self.chat_service = chat_service
        self.tts_service = tts_service
        self.translator = GoogleTranslator(source='en', target='pt')
        self.system_message = (
            "You are a creative video script writer. "
            "1. Generate a HIGHLY DETAILED image prompt (in English) for an AI image generator (16:9). "
            "2. Create a list of 5 to 7 short, educational English phrases."
        )

    def _download_image(self, image_link: str) -> str:
        file_path = Path(f'cache/images/temp_{uuid.uuid4()}.jpg')
        response = requests.get(image_link, stream=True)
        response.raise_for_status()
        with open(file_path, 'wb') as f:
            for chunk in response.iter_content(8192):
                f.write(chunk)
        return file_path
    
    def generate_video_content(self, user_input: str) -> VideoResponseStruct:
        return self.chat_service.generate_parsed(
            prompt=f'Create a video script based on this idea: {user_input}',
            response_schema=VideoResponseStruct,
            system_message=self.system_message
        )
        
    def create_video_from_input(self, content: VideoResponseStruct, video_size: Tuple[int, int] = (1280, 720)) -> str:
        w, h = video_size
        
        # Get freepik image
        image_info = generate_image_info(self.freepik_api, content.image_prompt)
        image_url = wait_for_image(self.freepik_api, image_info)
        img_path = self._download_image(image_url)
        
        # Background clip
        background_clip = ImageClip(img_path).resized(new_size=(w, h))
        
        steps = []
        for phrase in content.phrases:
            # Translate
            phrase_pt = self.translator.translate(phrase)
            
            # Audio generation Áudio
            temp_audio_path = Path(f'cache/audio/temp_audio_{uuid.uuid4()}.wav')
            self.tts_service.tts_file(temp_audio_path, phrase, speaker='af_heart')
            
            # Create audio clip
            audio_clip = concatenate_audioclips([
                AudioFileClip(temp_audio_path),
                AudioClip(lambda _: np.zeros((1, 2)), duration=2, fps=44100)
            ])
            
            # Create TextClips
            # English in yellow
            text_en = TextClip(
                text=phrase, font_size=70, color='yellow', font=None,
                method='caption', size=(int(w*0.9), 180), stroke_color='black', stroke_width=2, text_align='center'
            ).with_duration(audio_clip.duration).with_position(('center', int(h*0.35))).with_effects([FadeIn(0.5), FadeOut(0.5)])

            # Portuguese in white
            text_pt = TextClip(
                text=phrase_pt, font_size=50, color='white', font=None,
                method='caption', size=(int(w*0.9), 120), stroke_color='black', stroke_width=1, text_align='center'
            ).with_duration(audio_clip.duration).with_position(('center', int(h*0.65))).with_effects([FadeIn(0.5), FadeOut(0.5)])
             

            # Combine static image + audio + texts for this phrase
            slide = CompositeVideoClip(
                [background_clip.with_duration(audio_clip.duration), text_en, text_pt],
                size=(w, h)
            ).with_audio(audio_clip).with_duration(audio_clip.duration)
            
            steps.append(slide)

        # Concatenate video clips
        final_video = concatenate_videoclips(steps, method='compose')
        output_filename = Path(f'cache/videos/video_{uuid.uuid4()}.mp4')
        
        # Render final video
        final_video.write_videofile(output_filename, fps=20)
        
        # Cleanup
        if os.path.exists(img_path):
            os.remove(img_path)
        if os.path.exists(temp_audio_path):
            os.remove(temp_audio_path)
            
        return output_filename