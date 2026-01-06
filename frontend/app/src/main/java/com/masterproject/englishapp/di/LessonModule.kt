package com.masterproject.englishapp.di

import com.masterproject.englishapp.lessons.content.repository.InMemoryLessonRepository
import com.masterproject.englishapp.lessons.content.model.LessonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LessonModule {

    @Provides
    @Singleton
    fun provideLessonRepository(): LessonRepository {
        return InMemoryLessonRepository()
    }
}