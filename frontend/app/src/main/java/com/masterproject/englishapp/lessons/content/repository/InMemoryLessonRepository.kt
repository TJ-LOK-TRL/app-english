package com.masterproject.englishapp.lessons.content.repository

import com.masterproject.englishapp.lessons.content.model.LessonConfig
import com.masterproject.englishapp.lessons.content.model.LessonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryLessonRepository @Inject constructor() : LessonRepository {
    private val sessions = mutableMapOf<String, MutableStateFlow<List<LessonConfig>>>()

    override fun createSession(configs: List<LessonConfig>): String {
        val sessionId = UUID.randomUUID().toString()
        sessions[sessionId] = MutableStateFlow(configs)
        return sessionId
    }

    override fun getLessonConfigs(sessionId: String): StateFlow<List<LessonConfig>> {
        return sessions[sessionId]?.asStateFlow()
            ?: MutableStateFlow(emptyList<LessonConfig>()).asStateFlow()
    }
}