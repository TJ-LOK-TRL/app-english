package com.masterproject.englishapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import java.util.concurrent.TimeUnit

interface ApiService {
    @Multipart
    @POST("/api/speech/evaluate-pronunciation")
    suspend fun evaluatePronunciation(
        @Part audio: MultipartBody.Part,
        @Part("target_text") targetText: RequestBody
    ): PronunciationResult
}

data class PronunciationResult(
    val results: List<WordResult>
)

data class WordResult(
    val phonemes: List<String>,
    val score: Float,
    val label: String
)

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.246:8080"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)   // connection timeout
        .readTimeout(60, TimeUnit.SECONDS)      // read timeout
        .writeTimeout(60, TimeUnit.SECONDS)     // write timeout
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}