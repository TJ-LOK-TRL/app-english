package com.masterproject.englishapp.screens.exercises

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.masterproject.englishapp.components.FeedbackBottomBar
import com.masterproject.englishapp.components.animations.AnimatedStepContent
import com.masterproject.englishapp.components.headers.ExerciseHeader
import com.masterproject.englishapp.data.Category
import com.masterproject.englishapp.exercises.base.ExerciseInfo
import com.masterproject.englishapp.exercises.model.ExerciseKind
import com.masterproject.englishapp.exercises.model.ExerciseResult
import com.masterproject.englishapp.exercises.model.ExerciseType
import com.masterproject.englishapp.navigation.Navigator
import com.masterproject.englishapp.ui.theme.AppColors

@Composable
fun ExerciseFlowManager(
    navigator: Navigator,
    exerciseTypes: Set<ExerciseType> = emptySet(),
    categories: Set<Category> = emptySet(),
) {
    val allowedTypes = exerciseTypes.ifEmpty { ExerciseType.entries.toSet() }
    val allowedCategories = categories.ifEmpty { Category.entries.toSet() }

    val availableExercises = remember(allowedTypes) {
        ExerciseKind.entries.filter {
            it.supportedTypes.any { type -> type in allowedTypes }
        }
    }

    var currentExercise by remember {
        mutableStateOf(
            if (availableExercises.isNotEmpty()) availableExercises.random()
            else null
        )
    }

    if (currentExercise == null) {
        Log.w("ExerciseFlowManager", "No exercises available for types $allowedTypes and categories $allowedCategories")
        return Text("No exercises available")
    }

    var feedbackState by remember { mutableStateOf<ExerciseResult?>(null) }

    var currentStep by remember { mutableIntStateOf(1) }
    var questionId by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val category = remember(questionId) { allowedCategories.random() }

    val totalSteps = 12

    val next = { incrementStep: Boolean ->
        if (incrementStep) {
            if (currentStep < totalSteps) currentStep++
            else { /* Fim */ }
        }

        questionId = System.currentTimeMillis()
        currentExercise = availableExercises.random()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header
            ExerciseHeader(
                currentStep = currentStep,
                totalSteps = totalSteps,
                onCloseClick = { navigator.controller.navigateUp() },
                onMoreClick = { },
                trackBarColor = AppColors.Primary,
                trackBackgroundColor = AppColors.Gray400
            )

            // Exercise screen
            AnimatedStepContent(
                targetState = questionId,
                modifier = Modifier.weight(1f)
            ) { _ ->
                currentExercise?.screen(ExerciseInfo(category)) { result ->
                    when (result) {
                        is ExerciseResult.Correct -> feedbackState = result
                        is ExerciseResult.Skipped -> next(false)
                        is ExerciseResult.Wrong -> feedbackState = result
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = feedbackState != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            feedbackState?.let { result ->
                FeedbackBottomBar(
                    title = if (result is ExerciseResult.Correct) "Good! Meaning:" else "Incorrect",
                    message = result.message ?: "...",
                    isCorrect = result is ExerciseResult.Correct,
                    onContinue = {
                        feedbackState = null
                        next(true)
                    }
                )
            }
        }
    }
}