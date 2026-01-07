package com.masterproject.englishapp.screens.home

import androidx.lifecycle.ViewModel
import com.masterproject.englishapp.exercises.model.ExerciseType
import com.masterproject.englishapp.user.UserContext
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userContext: UserContext
) : ViewModel() {
    val user get() = userContext.currentUser

    /**
     * Returns the progress (0.0 a 1.0) for a type of exercise
     */
    fun getProgressForType(type: ExerciseType): Float {
        val stats = user?.statistics?.accuracyByType?.get(type.name)
        return if (stats != null && stats.total > 0) {
            stats.correct.toFloat() / stats.total.toFloat()
        } else {
            0f
        }
    }
}