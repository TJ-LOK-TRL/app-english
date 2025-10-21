# core/enums/lang.py
from enum import Enum

class Lang(str, Enum):
    EN_US = 'en-US'
    EN_GB = 'en-GB'
    EN_AU = 'en-AU'
    EN_IN = 'en-IN'
    PT_PT = 'pt-PT'
    PT_BR = 'pt-BR'
    FR_FR = 'fr-FR'
    ES_ES = 'es-ES'
    ES_MX = 'es-MX'
    HI_IN = 'hi-IN'