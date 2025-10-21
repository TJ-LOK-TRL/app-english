package com.masterproject.englishapp.recorder

// AudioRecorder.kt
interface AudioRecorder {
    fun startRecording(onAudioData: (FloatArray) -> Unit)
    fun stopRecording(): FloatArray?
    val isRecording: Boolean
}