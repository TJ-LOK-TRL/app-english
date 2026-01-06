package com.masterproject.englishapp.network

import com.masterproject.englishapp.network.model.ChatResult
import retrofit2.http.*
import com.masterproject.englishapp.network.model.ConverseResult
import com.masterproject.englishapp.network.model.MeaningLessonResponse
import com.masterproject.englishapp.network.model.PronunciationResult
import com.masterproject.englishapp.network.model.SynthesizeResult
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ApiService {
    @Multipart
    @POST("/api/speech/evaluate-pronunciation")
    suspend fun evaluatePronunciation(
        @Part audio: MultipartBody.Part,
        @Part("target_text") targetText: RequestBody
    ): PronunciationResult

    @Multipart
    @POST("/api/speech/converse")
    suspend fun converse(
        @Part audio: MultipartBody.Part,
        @Part("lang") lang: RequestBody? = null,
        @Part("voice") voice: RequestBody? = null,
        @Part("speed") speed: RequestBody? = null
    ): ConverseResult

    @FormUrlEncoded
    @POST("/api/speech/chat")
    suspend fun chat(
        @Field("text") text: String,
        @Field("lang") lang: String? = null,
        @Field("voice") voice: String? = null,
        @Field("speed") speed: String? = null
    ): ChatResult

    @FormUrlEncoded
    @POST("/api/speech/kokoro/synthesize")
    suspend fun synthesize(
        @Field("text") text: String,
        @Field("lang") lang: String? = null,
        @Field("voice") voice: String? = null,
        @Field("speed") speed: Float? = null
    ): SynthesizeResult

    @FormUrlEncoded
    @POST("/api/speech/generate-lesson")
    suspend fun generateLesson(
        @Field("context") context: String
    ): MeaningLessonResponse
}