# 🎓 AI-Driven English Learning Platform

An advanced mobile ecosystem for English language acquisition, featuring real-time pronunciation scoring, a 3D conversational avatar, and an on-demand AI video lesson generator. This project integrates over 10 distinct AI models to provide a multimodal learning experience.

---

### 🎥 Demo Video

[![Watch the demo](https://img.youtube.com/vi/HEdq7SvWn1c/hqdefault.jpg)](https://youtu.be/HEdq7SvWn1c)

> Click the image to watch the demo video on YouTube.

---

## 🚀 Core Features

- **3D Conversational Avatar**: Real-time spoken interaction powered by the Gemini API.  
- **Pronunciation Assessment**: Phonetic-level scoring using Kaldi-GOP.  
- **On-Demand Video Lessons**: Generates full AI-powered educational videos with synchronized audio, images, and subtitles from text prompts.  
- **Contextual Learning (Object Recognition)**: Identifies real-world objects using the device camera to teach English vocabulary in context.  
- **Adaptive Learning (Digital Twin)**: Models the learner's progress and adapts difficulty dynamically using Bayesian Knowledge Tracing (BKT).  
- **Geofencing Tasks**: Location-based notifications trigger exercises using GPS.  
- **Statistics Dashboard**: Tracks performance metrics and learning history.  
- **User Profile & Authentication**: Stores progress, history, and allows login/account management.  
- **Multimodal Feedback**: Visual, auditory, and haptic feedback for reinforcement.  
- **Exercise Variety**: Writing, listening, reading comprehension, and pronunciation activities.  
- **Text-Based Chat & AI Practice**: Optional TTS playback for conversational practice.  
- **Curated Learning Videos**: Integration with YouTube playlists for sequential English practice.  
- **Daily Reminder Notifications**: Encourage regular practice.  

---

## 🧠 The AI Stack (10 Models Architecture)

This platform utilizes a sophisticated mix of Cloud-based and On-Device (Edge AI) models to balance performance and privacy.

### 🎙️ Speech & Audio Processing

- **Whisper-Tiny (OpenAI)**: Used for Automatic Speech Recognition (ASR). Note: Optimized for mobile via C++ implementation.  
- **Kaldi TDNN Acoustic Model**: Maps sound waves to phonemes for precise pronunciation analysis.  
- **Kaldi i-vector (Speaker Embedding)**: Adapts the system to the user's specific voice characteristics (Voice-to-Vector).  
- **Kaldi HMM (Sequential Modeling)**: Handles temporal structure and phoneme alignment for accurate GoP (Goodness of Pronunciation) calculation.  
- **Kokoro TTS**: A next-gen Text-to-Speech model (based on StyleTTS2) for natural and expressive audio feedback.  

### 👁️ Computer Vision & Image Generation

- **EfficientDet (Edge AI)**: Real-time object detection running directly on the device.  
- **EfficientNet (Edge AI)**: High-accuracy image classification for contextual vocabulary.  
- **Mystic (Freepik AI)**: State-of-the-art image generation used to create visual assets for custom video lessons.  

### 🤖 LLM & Adaptive Intelligence

- **Gemini API (Google)**: The "brain" behind the 3D avatar’s NLU (Natural Language Understanding) and video script generation.  
- **Bayesian Knowledge Tracing (BKT)**: Executed on the edge to maintain a Digital Twin of the user’s knowledge state.  

---

## 🧰 Requirements

### Functional Requirements

- Provide real-time pronunciation evaluation using ASR (Kaldi-GOP).  
- Enable spoken interaction with a 3D avatar capable of understanding and generating speech.  
- Support manual and automatic recording modes for avatar interaction.  
- Generate adaptive English-learning exercises based on user profile and performance.  
- Allow object detection with optional focused area selection for improved accuracy.  
- Maintain a user profile storing progress, history, and difficulty adaptations.  
- Generate AI video lessons from text prompts with synchronized speech, imagery, and subtitles.  
- Use geofencing and GPS to trigger location-based exercises.  
- Provide a statistics dashboard to track performance metrics.  
- User authentication system with login and account management.  
- Settings interface for preferences and app behavior.  
- Text-based chat interface for practicing written English with optional TTS.  
- Integrated YouTube learning section with curated video lists.  
- Daily reminder notifications.  
- Multimodal feedback (animations, sound, vibration).  
- Replay AI-generated videos without regeneration.  


### Non-Functional Requirements

- Real-time or near real-time feedback for speech and object recognition.  
- High accuracy in pronunciation scoring and object detection.  
- Intuitive and engaging user interface.  
- Data privacy, especially for audio and camera inputs.  
- Scalable to support many users and diverse learning content.  
- Efficient handling and playback of generated video lessons on mobile devices.  
- Fast generation and playback of pronunciation feedback.  
- Optimized resource usage for battery and device heat.  
- High system reliability and availability.  
- Smooth and responsive interactions with AI models.

## 🏃 How to Run the Project

### 🖥️ Backend (FastAPI + Uvicorn)

The backend runs inside a Docker container that includes Kaldi and the Kaldi-GOP binaries.

⚠️ The Docker image is private. If you need it, contact the maintainer to obtain it.

> Note: Kaldi/GOP is **only required for the Pronunciation feature**.  
> If you do not want to use this feature, you can run the project on any system and simply disable or ignore the pronunciation functionality.

---

#### Using the provided container image

1. Run the container:
    docker run -it --name app_english_backend --gpus all -v ${PWD}/backend/app:/usr/src/app -p 8080:8080 backend-base /bin/bash

2. Start and enter the container:
    docker start -ai app_english_backend
    docker exec -it app_english_backend /bin/bash

4. Navigate to the backend folder:

    cd /usr/src/app

5. Install all dependencies:

    python3 -m pip install -r requirements.txt

6. Run the backend server:

    uvicorn app:app --host 0.0.0.0 --port 8080 --reload

> The backend will now listen on your local network. Make sure to note the IP address.

---

#### Building your own container (advanced)

Building your own container with Kaldi + Kaldi-GOP requires advanced knowledge:

- You need a **Kaldi base image** and must **compile Kaldi** with proper dependencies.  
- Only the following **folders and binaries** are essential for Kaldi-GOP:

Folders:

    base matrix util fstext lat tree gmm decoder transform lm feat
    online2 ivector nnet3 chain nnet2 cudamatrix

Binaries:

    bin/compute-gop
    bin/ali-to-phones
    bin/compile-train-graphs
    bin/compile-train-graphs-without-lexicon
    bin/align-compiled-mapped
    featbin/compute-mfcc-feats
    featbin/copy-feats
    featbin/apply-cmvn
    nnet3bin/nnet3-compute
    nnet3bin/nnet3-align-compiled
    online2bin/ivector-extract-online2

⚠️ Note: This avoids installing unnecessary Kaldi binaries (~20 GB).  
⚠️ You are responsible for compiling and configuring Kaldi and Kaldi-GOP correctly if building your own image.

### 📱 Kotlin Frontend

1. Open the project in IntelliJ / Android Studio.  
2. In `java/main`, locate the `RetrofitClient` object under `network` package:
```
private const val BASE_URL = "http://192.168.1.247:8080" # <-- Replace with your backend IP if different
```
3. Make sure the `BASE_URL` matches the backend IP and port.  
4. Run the app by clicking the **Run** button in Kotlin/Android Studio.  

> The app will communicate with the backend to provide AI-driven English learning features.
