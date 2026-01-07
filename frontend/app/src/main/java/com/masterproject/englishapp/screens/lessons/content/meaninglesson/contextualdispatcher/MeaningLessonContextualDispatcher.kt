package com.masterproject.englishapp.screens.lessons.content.meaninglesson.contextualdispatcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.components.loaders.OverlayLoader
import com.masterproject.englishapp.navigation.NavigationActions
import com.masterproject.englishapp.navigation.Screen

@Composable
fun MeaningLessonContextualDispatcher(
    navigator: NavigationActions,
    viewModel: MeaningLessonContextualViewModel = hiltViewModel()
) {
    val sessionId by viewModel.sessionId.collectAsState()
    val isError by viewModel.isError.collectAsState()

    LaunchedEffect(sessionId) {
        sessionId?.let {
            navigator.navigate(Screen.LESSONS, "/$sessionId")
        }
    }

    LaunchedEffect(isError) {
        if (isError) {
            navigator.navigate(Screen.HOME)
        }
    }

    OverlayLoader()
}