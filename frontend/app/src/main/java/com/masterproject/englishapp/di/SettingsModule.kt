package com.masterproject.englishapp.di

import com.masterproject.englishapp.user.UserPreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {
    @Provides
    @Singleton
    fun provideUserPreferencesStore(): UserPreferencesStore = UserPreferencesStore()
}