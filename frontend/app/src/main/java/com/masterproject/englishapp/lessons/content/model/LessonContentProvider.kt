package com.masterproject.englishapp.lessons.content.model

import com.masterproject.englishapp.data.providers.ContentProvider

interface LessonContentProvider<T> : ContentProvider {
    suspend fun getContent(lessonConfig: LessonConfig): T
}