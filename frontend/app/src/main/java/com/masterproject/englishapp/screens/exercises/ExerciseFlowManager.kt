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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.R
import com.masterproject.englishapp.audio.SoundManager
import com.masterproject.englishapp.components.FeedbackBottomBar
import com.masterproject.englishapp.components.animations.AnimatedStepContent
import com.masterproject.englishapp.components.headers.ExerciseHeader
import com.masterproject.englishapp.grammar.Category
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
    sessionViewModel: ExerciseSessionViewModel = hiltViewModel()
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

    val startTime = remember { System.currentTimeMillis() }
    val answersHistory = remember { mutableStateListOf<Boolean>() }
    var isFinished by remember { mutableStateOf(false) }

    val formattedTime = remember(isFinished) {
        if (!isFinished) "0:00"
        else {
            val totalSeconds = (System.currentTimeMillis() - startTime) / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            "%d:%02d".format(minutes, seconds)
        }
    }

    val accuracyScore = remember(isFinished) {
        if (answersHistory.isEmpty()) 0
        else {
            val correctOnes = answersHistory.count { it }
            (correctOnes * 100) / answersHistory.size
        }
    }

    var feedbackState by remember { mutableStateOf<ExerciseResult?>(null) }

    var questionId by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val category = remember(questionId) { allowedCategories.random() }

    var currentStep by remember { mutableIntStateOf(0) }
    val totalSteps = 11

    val next = { incrementStep: Boolean ->
        if (incrementStep) {
            if (currentStep < totalSteps) {
                currentStep++
                questionId = System.currentTimeMillis()
                currentExercise = availableExercises.random()
            } else {
                val sessionDuration = System.currentTimeMillis() - startTime
                sessionViewModel.handleExerciseFlowEnd(sessionDuration)
                isFinished = true
            }
        } else {
            questionId = System.currentTimeMillis()
            currentExercise = availableExercises.random()
        }
    }

    val context = LocalContext.current
    val feedbackManager = remember { SoundManager(context) }

    LaunchedEffect(feedbackState) {
        val result = feedbackState ?: return@LaunchedEffect

        if (sessionViewModel.isSoundEnabled) {
            when (result) {
                is ExerciseResult.Correct -> feedbackManager.playSound(R.raw.correct)
                is ExerciseResult.Wrong -> feedbackManager.playSound(R.raw.wrong)
                else -> {}
            }
        }
    }

    LaunchedEffect(isFinished) {
        if (isFinished && sessionViewModel.isVibrationEnabled) {
            feedbackManager.vibrate()
        }
    }

    if (isFinished) {
        ExerciseEndScreen(
            timeElapsed = formattedTime,
            accuracy = accuracyScore,
            onEndClick = { navigator.controller.navigateUp() }
        )
    } else {
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
                ) { targetId ->
                    val exerciseForStep = remember(targetId) { currentExercise }

                    exerciseForStep?.screen(ExerciseInfo(category)) { result ->
                        when (result) {
                            is ExerciseResult.Correct -> feedbackState = result
                            is ExerciseResult.Skipped -> next(false)
                            is ExerciseResult.Wrong -> feedbackState = result
                            is ExerciseResult.Error -> next(false)
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
                        title = if (result is ExerciseResult.Correct) "Good!" else "Incorrect",
                        message = result.message ?: "",
                        isCorrect = result is ExerciseResult.Correct,
                        onContinue = {
                            sessionViewModel.handleExerciseResult(result, currentExercise!!.supportedTypes.random())
                            answersHistory.add(result is ExerciseResult.Correct)
                            feedbackState = null
                            next(true)
                        }
                    )
                }
            }
        }
    }
}