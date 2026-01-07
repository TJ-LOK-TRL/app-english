package com.masterproject.englishapp.user

data class UserStatistics(
    val longestStreak: Int = 0,
    val currentStreak: Int = 0,
    val totalLearningDays: Int = 0,
    val lastLessonDate: Long? = null, // Timestamp (ms)
    val lessonsPassed: Int = 0,
    val lessonsFailed: Int = 0,
    val accuracyByType: Map<String, AccuracyStats> = emptyMap(),
    val totalTimeSpentMs: Long = 0,
    val averageLessonTimeMs: Long = 0
)

data class AccuracyStats(
    val correct: Int = 0,
    val total: Int = 0
) {
    val percentage: Int get() = if (total == 0) 0 else (correct * 100) / total
}