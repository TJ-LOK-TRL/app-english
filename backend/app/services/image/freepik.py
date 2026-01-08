from time import sleep
from typing import Any, Dict, List, Optional
import requests

def generate_image_info(
    api_key: str,
    prompt: str,
    webhook_url: Optional[str] = None,
    structure_reference: Optional[str] = None,
    structure_strength: Optional[int] = None,
    style_reference: Optional[str] = None,
    adherence: Optional[int] = None,
    hdr: Optional[int] = None,
    resolution: str = "2k", # 1k 2k 4k
    aspect_ratio: str = "widescreen_16_9",
    model: str = "zen",
    creative_detailing: int = 33, # [0, 100]
    engine: str = "automatic", # automatic, magnific_illusio, magnific_sharpy, magnific_sparkle 
    fixed_generation: bool = False,
    filter_nsfw: bool = True,
    styling_styles: Optional[List[Dict[str, Any]]] = None,
    styling_characters: Optional[List[Dict[str, Any]]] = None,
    styling_colors: Optional[List[Dict[str, Any]]] = None
) -> Dict[str, Any]:
    """
    Faz o POST para a API Freepik Mystic com os parâmetros fornecidos e retorna o JSON da resposta.

    Args:
        api_key: Chave da API Freepik.
        prompt: Texto do prompt para gerar a imagem.
        webhook_url: URL para webhook de callback.
        structure_reference: Referência da estrutura (string base64).
        structure_strength: Força da estrutura (0-100).
        style_reference: Referência de estilo (string base64).
        adherence: Grau de aderência ao estilo (0-100).
        hdr: HDR level (0-100).
        resolution: Resolução da imagem ('2k', '4k', etc).
        aspect_ratio: Proporção da imagem ('square_1_1', '16_9', etc).
        model: Modelo a usar ('realism', etc).
        creative_detailing: Detalhamento criativo (0-100).
        engine: Motor a usar ('automatic', etc).
        fixed_generation: Boolean para geração fixa.
        filter_nsfw: Boolean para filtrar conteúdo NSFW.
        styling_styles: Lista de estilos com nome e força.
        styling_characters: Lista de personagens com id e força.
        styling_colors: Lista de cores com cor e peso.

    Returns:
        Resposta JSON da API.
    """

    url = "https://api.freepik.com/v1/ai/mystic"
    headers = {
        "Content-Type": "application/json",
        "x-freepik-api-key": api_key
    }

    payload = {
        k: v for k, v in {
            "prompt": prompt,
            "webhook_url": webhook_url,
            "structure_reference": structure_reference,
            "structure_strength": structure_strength,
            "style_reference": style_reference,
            "adherence": adherence,
            "hdr": hdr,
            "resolution": resolution,
            "aspect_ratio": aspect_ratio,
            "model": model,
            "creative_detailing": creative_detailing,
            "engine": engine,
            "fixed_generation": fixed_generation,
            "filter_nsfw": filter_nsfw,
            #"styling": {
            #    "styles": styling_styles or [],
            #    "characters": styling_characters or [],
            #    "colors": styling_colors or []
            #}
        }.items() if v is not None
    }
    response = requests.post(url, headers=headers, json=payload)
    response.raise_for_status()
    return response.json()

def check_task_status(api_key: str, task_id: str):
    url = f'https://api.freepik.com/v1/ai/mystic/{task_id}'
    headers = {
        'x-freepik-api-key': api_key
    }

    response = requests.get(url, headers=headers)
    response.raise_for_status()
    return response.json()

def wait_for_image(api_key: str, info: Dict[str, Any], interval: float = 5) -> str:
    while info['data']['status'] != 'COMPLETED':
        sleep(interval)
        info = check_task_status(api_key, info['data']['task_id'])
        print(info)
    return info['data']['generated'][0]