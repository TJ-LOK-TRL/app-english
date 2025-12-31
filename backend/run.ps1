docker run -it --name app_english_backend --gpus all -v ${PWD}\backend\app:/usr/src/app -p 8080:8080 app-english-backend:latest /bin/bash # BASE
docker run -it --name app_english_backend --gpus all -v ${PWD}\backend\app:/usr/src/app -p 8080:8080 kaldi-base:latest /bin/bash # KALDI BASE
docker run -it --name app_english_backend --gpus all -v ${PWD}\backend\app:/usr/src/app -p 8080:8080 backend-base /bin/bash # BACKEND BASE

docker run -it --name app_english_backend --gpus all -v ${PWD}\backend\app:/usr/src/app -p 8080:8080 backend-base /bin/bash
docker start -it app_english_backend 
docker exec -it app_english_backend /bin/bash
cd /usr/src/app
uvicorn main:app --host 0.0.0.0 --port 8080 --reload