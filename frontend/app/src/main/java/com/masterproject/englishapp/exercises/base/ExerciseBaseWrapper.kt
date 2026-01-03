package com.masterproject.englishapp.exercises.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.components.loaders.ExerciseLoadingScreen
import com.masterproject.englishapp.exercises.model.ExerciseResult

@Composable
inline fun <T, reified VM> ExerciseBaseWrapper(
    exerciseInfo: ExerciseInfo,
    viewModel: VM = hiltViewModel(),
    crossinline onResult: (ExerciseResult) -> Unit,
    content: @Composable (T) -> Unit
) where VM : BaseExerciseViewModel<T> {
    val data = viewModel.uiState
    val error = viewModel.hasError

    LaunchedEffect(Unit) {
        viewModel.loadNext(exerciseInfo)
    }

    LaunchedEffect(error) {
        if (error) {
            onResult(ExerciseResult.Error())
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (error || data == null) {
            ExerciseLoadingScreen()
        } else {
            key(data) {
                content(data)
            }
        }
    }
}