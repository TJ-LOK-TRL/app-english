package com.masterproject.englishapp.screens.lessons.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.masterproject.englishapp.components.animations.AnimatedStepContent
import com.masterproject.englishapp.components.buttons.PrimaryButton
import com.masterproject.englishapp.components.headers.ExerciseHeader
import com.masterproject.englishapp.components.loaders.AppSplashScreen
import com.masterproject.englishapp.lessons.content.meaninglesson.MeaningLessonData
import com.masterproject.englishapp.lessons.content.model.LessonStep
import com.masterproject.englishapp.navigation.NavigationActions
import com.masterproject.englishapp.navigation.Navigator
import com.masterproject.englishapp.screens.lessons.content.meaninglesson.MeaningLessonContent
import com.masterproject.englishapp.ui.theme.AppColors
import com.masterproject.englishapp.utils.DummyMeaningLessonData
import com.masterproject.englishapp.utils.DummyNavigator

@Composable
fun LessonFlowManager(
    navigator: Navigator,
    viewModel: LessonFlowViewModel = hiltViewModel()
) {
    val steps by viewModel.steps.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LessonFlowManagerContent(
        steps = steps,
        isLoading = isLoading,
        navigator = navigator
    )
}

@Composable
fun LessonFlowManagerContent(
    steps: List<LessonStep>,
    isLoading: Boolean,
    navigator: NavigationActions
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var showEndScreen by remember { mutableStateOf(false) }

    if (isLoading) {
        AppSplashScreen()
    } else if (steps.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No lessons available.")
        }
    } else if (showEndScreen) {
        LessonEndScreen { navigator.navigateUp() }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            ExerciseHeader(
                currentStep = currentIndex + 1,
                totalSteps = steps.size,
                onCloseClick = { navigator.navigateUp() },
                onMoreClick = { },
                trackBarColor = AppColors.Primary,
                trackBackgroundColor = AppColors.Gray400
            )

            Box(modifier = Modifier.weight(1f)) {
                AnimatedStepContent(targetState = currentIndex) { index ->
                    val step = steps[index]

                    when (step) {
                        is MeaningLessonData -> {
                            MeaningLessonContent(data = step)
                        }
                    }
                }
            }

            PrimaryButton(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                text = if (currentIndex < steps.size - 1) "Continuar" else "Finalizar Lição",
                onClick = {
                    if (currentIndex < steps.size - 1) {
                        currentIndex++
                    } else {
                        showEndScreen = true
                    }
                }
            )
        }
    }
}


@Preview(
    name = "Xiaomi Redmi 9C",
    device = "spec:width=360dp,height=800dp,dpi=269",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFEEEEEE
)
@Composable
fun LessonFlowPreview() {
    val mockSteps = listOf(
        MeaningLessonData(
            learningPhrase = "The sun is shining",
            feedbackPhrase = "O sol está a brilhar",
            explanation = "Daily weather context",
            contextTitle = "Weather"
        ),
        MeaningLessonData(
            learningPhrase = "It's raining cats and dogs",
            feedbackPhrase = "Está a chover a potes",
            explanation = "Idiom for heavy rain",
            contextTitle = "Weather"
        ),

        DummyMeaningLessonData
    )

    LessonFlowManagerContent(
        steps = mockSteps,
        isLoading = false,
        DummyNavigator
    )
}