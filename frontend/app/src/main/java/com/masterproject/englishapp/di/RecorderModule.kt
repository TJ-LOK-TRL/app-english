package com.masterproject.englishapp.di

import android.content.Context
import com.masterproject.englishapp.recorder.AndroidAudioRecorder
import com.masterproject.englishapp.recorder.AudioRecorder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecorderModule {

    @Provides
    @Singleton
    fun provideRecorder(@ApplicationContext context: Context): AudioRecorder = AndroidAudioRecorder(context)
}