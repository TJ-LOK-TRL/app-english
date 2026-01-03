package com.masterproject.englishapp.screens.exercises

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterproject.englishapp.exercises.model.ExerciseResult
import com.masterproject.englishapp.learning.core.SkillKey
import com.masterproject.englishapp.user.UserContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseSessionViewModel @Inject constructor(
    private val userContext: UserContext
) : ViewModel() {

    val knowledgeModel get() = userContext.model

    fun updateKnowledge(result: ExerciseResult) {
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

    fun saveFinalProgress() {
        viewModelScope.launch(Dispatchers.IO) {
            userContext.saveProgress()
        }
    }
}