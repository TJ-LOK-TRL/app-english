package com.masterproject.englishapp.screens.exercises

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.exercises.model.ExerciseResult
import com.masterproject.englishapp.exercises.model.ExerciseType
import com.masterproject.englishapp.learning.core.SkillKey
import com.masterproject.englishapp.user.AccuracyStats
import com.masterproject.englishapp.user.UserContext
import com.masterproject.englishapp.user.UserPreferencesStore
import com.masterproject.englishapp.utils.checkIsSameDay
import com.masterproject.englishapp.utils.checkIsStreakContinued
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseSessionViewModel @Inject constructor(
    private val userContext: UserContext,
    private val userPreferencesStore: UserPreferencesStore,
) : ViewModel() {

    val knowledgeModel get() = userContext.model

    val user get() = userContext.currentUser

    val isSoundEnabled get() = userPreferencesStore.soundEffectsEnabled
    val isVibrationEnabled get() = userPreferencesStore.vibrationEnabled

    fun handleExerciseResult(result: ExerciseResult, type: ExerciseType) {
        updateKnowledge(result)
        updateAccuracyStats(result, type)
    }

    fun handleExerciseFlowEnd(durationMs: Long) {
        updateStreakAndLessonStats(durationMs)
        saveFinalProgress()
    }

    private fun updateKnowledge(result: ExerciseResult) {
        if (result.skillIds.isEmpty()) return
        val success = result is ExerciseResult.Correct

        result.skillIds.forEach { id ->
            knowledgeModel?.update(
                skill = SkillKey(id),
                success = success
            )
            Log.d("BKT_UPDATE", "Skill: $id | Success: $success")
        }

        Log.d("BKT_UPDATE_SUMMARY", "Updated ${result.skillIds.size} skills. Result was: ${if(success) "Success" else "Failure"}")
    }

    private fun updateAccuracyStats(result: ExerciseResult, type: ExerciseType) {
        val currentUser = user ?: return
        val stats = currentUser.statistics
        val typeKey = type.name
        val success = result is ExerciseResult.Correct

        val currentAccuracyStats = stats.accuracyByType[typeKey] ?: AccuracyStats()
        val updatedAccuracyStats = currentAccuracyStats.copy(
            total = currentAccuracyStats.total + 1,
            correct = currentAccuracyStats.correct + if (success) 1 else 0
        )

        val updatedMap = stats.accuracyByType.toMutableMap().apply {
            put(typeKey, updatedAccuracyStats)
        }

        val updatedUser = currentUser.copy(
            statistics = stats.copy(accuracyByType = updatedMap)
        )

        userContext.setUser(updatedUser)
    }

    private fun updateStreakAndLessonStats(durationMs: Long) {
        val currentUser = user ?: return
        val now = System.currentTimeMillis()
        val stats = currentUser.statistics

        // Verify if is already a new day in relation with the last lesson
        val isSameDay = checkIsSameDay(stats.lastLessonDate, now)
        val isNextDay = checkIsStreakContinued(stats.lastLessonDate, now)

        val newStreak = when {
            isSameDay -> stats.currentStreak
            isNextDay -> stats.currentStreak + 1
            else -> 1
        }

        val newTotalLessons = stats.lessonsPassed + 1
        val newTotalTime = stats.totalTimeSpentMs + durationMs

        val updatedUser = currentUser.copy(
            statistics = stats.copy(
                lastLessonDate = now,
                currentStreak = newStreak,
                longestStreak = maxOf(stats.longestStreak, newStreak),
                totalLearningDays = if (isSameDay) stats.totalLearningDays else stats.totalLearningDays + 1,
                lessonsPassed = stats.lessonsPassed + 1,
                totalTimeSpentMs = newTotalTime,
                averageLessonTimeMs = newTotalTime / newTotalLessons
            )
        )
        userContext.setUser(updatedUser)
    }

    private fun saveFinalProgress() {
        viewModelScope.launch(Dispatchers.IO) {
            userContext.saveProgress()
        }
    }
}