package com.masterproject.englishapp.di

import com.masterproject.englishapp.network.ApiService
import com.masterproject.englishapp.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApi(): ApiService = RetrofitClient.api
}