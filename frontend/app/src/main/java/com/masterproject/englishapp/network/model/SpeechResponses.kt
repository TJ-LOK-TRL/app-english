package com.masterproject.englishapp.network.model

import com.google.gson.annotations.SerializedName

// --- SHARED MODELS ---
data class ASRSegment(
    val text: String,
    val whitespace: Boolean,
    @SerializedName("start_ts") val startTs: Float,
    @SerializedName("end_ts") val endTs: Float,
    val phonemes: String?
)

data class PronunciationResult(
    val results: List<WordResult>
)

data class WordResult(
    val phonemes: List<String>,
    val score: Float,
    val label: String
)

data class ConverseResult(
    val text: String,
    val audio: String, // base64
    val tokens: List<ASRSegment>,
    @SerializedName("pred_dur") val predDur: List<Float>
)

data class ChatResult(
    val text: String,
    val audio: String,
    val tokens: List<ASRSegment>,
    @SerializedName("pred_dur") val predDur: List<Float>
)

data class SynthesizeResult(
    val audio: String, // base64
    @SerializedName("sample_rate") val sampleRate: Int,
    val tokens: List<ASRSegment>,
    @SerializedName("pred_dur") val predDur: List<Float>
)

data class MeaningLessonResponse(
    @SerializedName("context_title") val contextTitle: String,
    val items: List<MeaningLessonItem>
)

data class MeaningLessonItem(
    val original: String,
    val translated: String,
    val explanation: String
)