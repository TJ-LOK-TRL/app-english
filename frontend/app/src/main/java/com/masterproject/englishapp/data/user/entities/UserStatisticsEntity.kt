package com.masterproject.englishapp.data.user.entities

data class UserStatisticsEntity(
    val longestStreak: Int = 0,
    val currentStreak: Int = 0,
    val totalLearningDays: Int = 0,
    val lastLessonDate: Long? = null,
    val lessonsPassed: Int = 0,
    val lessonsFailed: Int = 0,
    val accuracyByType: Map<String, AccuracyStatsEntity> = emptyMap(),
    val totalTimeSpentMs: Long = 0,
    val averageLessonTimeMs: Long = 0
)

data class AccuracyStatsEntity(
    val correct: Int = 0,
    val total: Int = 0
)
