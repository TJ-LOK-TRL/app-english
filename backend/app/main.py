from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from api.endpoints import speech

app = FastAPI(title='AppIngles API', version='1.0.0')

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=['*'],  # In production, specify domains
    allow_credentials=True,
    allow_methods=['*'],
    allow_headers=['*'],
)

# Include routes
app.include_router(speech.router, prefix='/api/speech', tags=['speech'])
#app.include_router(camera.router, prefix='/api/camera', tags=['camera'])
#app.include_router(location.router, prefix='/api/location', tags=['location'])
#app.include_router(lessons.router, prefix='/api/lessons', tags=['lessons'])

@app.get("/")
async def root():
    return {'message': 'AppIngles API is working!'}