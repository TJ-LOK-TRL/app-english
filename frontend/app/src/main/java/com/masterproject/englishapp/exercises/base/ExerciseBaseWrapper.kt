package com.masterproject.englishapp.exercises.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.masterproject.englishapp.components.loaders.ExerciseLoadingScreen

@Composable
inline fun <T, reified VM> ExerciseBaseWrapper(
    exerciseInfo: ExerciseInfo,
    viewModel: VM = hiltViewModel(),
    content: @Composable (T) -> Unit
) where VM : ViewModel, VM : ExerciseViewModel<T> {
    val data = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadNext(exerciseInfo)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (data == null) {
            ExerciseLoadingScreen()
        } else {
            key(data) {
                content(data)
            }
        }
    }
}