docker run -it --name app_english_backend --gpus all -v ${PWD}\backend\app:/usr/src/app -p 8080:8080 app-english-backend:latest /bin/bash # BASE
docker run -it --name app_english_backend --gpus all -v ${PWD}\backend\app:/usr/src/app -p 8080:8080 kaldi-base:latest /bin/bash # KALDI BASE
