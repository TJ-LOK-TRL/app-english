package com.masterproject.englishapp.lessons.content.model

import kotlinx.coroutines.flow.StateFlow

interface LessonRepository {
    fun createSession(configs: List<LessonConfig>): String
    fun getLessonConfigs(sessionId: String): StateFlow<List<LessonConfig>>
}