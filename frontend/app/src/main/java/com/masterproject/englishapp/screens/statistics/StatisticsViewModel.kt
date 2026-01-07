package com.masterproject.englishapp.screens.statistics

import android.util.Log
import androidx.lifecycle.ViewModel
import com.masterproject.englishapp.event.UiEventService
import com.masterproject.englishapp.exercises.model.ExerciseType
import com.masterproject.englishapp.user.UserContext
import com.masterproject.englishapp.user.UserStatistics
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val userContext: UserContext,
    val uiEventService: UiEventService
) : ViewModel() {

    val user get() = userContext.currentUser

    /**
     * Maps real data to RadarChart ensuring all 6 categories exist.
     * Returns a map with exercise type as key and accuracy percentage as value.
     */
    fun getRadarStats(stats: UserStatistics?): Map<String, Float> {
        val accuracyMap = stats?.accuracyByType ?: emptyMap()

        // Helper function to get percentage for a specific type or return 0
        fun getPercentage(type: ExerciseType): Float {
            val data = accuracyMap[type.name]
            return if (data != null && data.total > 0) {
                data.correct.toFloat() / data.total.toFloat()
            } else 0f
        }

        // 1. Actual measured values
        val writePerc = getPercentage(ExerciseType.WRITE)
        val speakPerc = getPercentage(ExerciseType.SPEAK)
        val listenPerc = getPercentage(ExerciseType.LISTENING)
        val comprehensionPerc = getPercentage(ExerciseType.COMPREHENSION)

        // 2. Calculated composite values
        // Grammar: Blend of Writing and Comprehension (grammatical reasoning foundation)
        val grammarPerc = (writePerc + comprehensionPerc) / 2f

        // Vocabulary: Blend of Listening and Writing (word recognition and production)
        val vocabularyPerc = (listenPerc + writePerc) / 2f

        // Returns fixed map with 6 positions for the radar chart
        return mapOf(
            "Writing" to writePerc,
            "Speaking" to speakPerc,
            "Listening" to listenPerc,
            "Comprehension" to comprehensionPerc,
            "Grammar" to grammarPerc,
            "Vocabulary" to vocabularyPerc
        )
    }

    /**
     * Calculates total number of exercises completed across all types.
     */
    fun getTotalExercises(stats: UserStatistics?): Int {
        return stats?.accuracyByType?.values?.sumOf { it.total } ?: 0
    }

    /**
     * Calculates total number of correct exercises globally.
     */
    fun getTotalCorrect(stats: UserStatistics?): Int {
        return stats?.accuracyByType?.values?.sumOf { it.correct } ?: 0
    }

    /**
     * Formats average time from milliseconds to minutes.
     * Returns the time in minutes (rounded down).
     */
    fun getFormattedAvgTime(stats: UserStatistics?): Int {
        val totalMs = stats?.averageLessonTimeMs ?: 0L
        return (totalMs / 60000).toInt()
    }

    /**
     * Total words in the model.
     * Returns the number of words discovered.
     */
    fun getWordsDiscoveredCount(): Int {
        val state = userContext.model?.getState() ?: return 0

        return state.keys.count { skillKey ->
            val id = skillKey.value
            val words = id.trim().split(Regex("\\s+"))
            words.size == 1 && words[0].isNotEmpty()
        }
    }
}