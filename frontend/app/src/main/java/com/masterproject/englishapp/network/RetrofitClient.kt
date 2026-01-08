package com.masterproject.englishapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.247:8080"
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.MINUTES)   // connection timeout
        .readTimeout(5, TimeUnit.MINUTES)      // read timeout
        .writeTimeout(5, TimeUnit.MINUTES)     // write timeout
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