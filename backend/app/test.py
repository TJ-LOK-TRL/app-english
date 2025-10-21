from services.tts.kokoro import KokoroTTSService, KokoroVoice

text = 'Tomorrow I would went by the school and I will study'
KokoroTTSService().tts_file('usr_audo_test3.wav', text, speaker=KokoroVoice.AMERICAN_FEMALE_HEART)