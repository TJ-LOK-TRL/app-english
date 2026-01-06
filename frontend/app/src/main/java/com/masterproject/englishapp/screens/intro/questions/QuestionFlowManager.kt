package com.masterproject.englishapp.screens.intro.questions

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavOptionsBuilder
import com.masterproject.englishapp.components.animations.AnimatedStepContent
import com.masterproject.englishapp.components.buttons.PrimaryButton
import com.masterproject.englishapp.components.headers.ProgressHeader
import com.masterproject.englishapp.grammar.Language
import com.masterproject.englishapp.navigation.NavigationActions
import com.masterproject.englishapp.navigation.Screen

enum class QuestionStep { STARTER, LEARNING_LANG, FEEDBACK_LANG, ENDER }

@Composable
fun QuestionFlowManager(
    navigator: NavigationActions,
    viewModel: QuestionViewModel = hiltViewModel()
) {
    QuestionFlowManagerContent(
        selectedLearningLanguage = viewModel.preferencesStore.learningLanguage,
        selectedFeedbackLanguage = viewModel.preferencesStore.feedbackLanguage,
        onLearningLanguageSelected = { viewModel.updateLearningLanguage(it) },
        onFeedbackLanguageSelected = { viewModel.updateFeedbackLanguage(it) },
        onFinish = { navigator.navigate(Screen.REGISTER) }
    )
}

@Composable
fun QuestionFlowManagerContent(
    selectedLearningLanguage: Language?,
    selectedFeedbackLanguage: Language?,
    onLearningLanguageSelected: (Language) -> Unit,
    onFeedbackLanguageSelected: (Language) -> Unit,
    onFinish: () -> Unit
) {
    var currentStep by remember { mutableStateOf(QuestionStep.STARTER) }

    val enableButton = when (currentStep) {
        QuestionStep.STARTER -> true
        QuestionStep.LEARNING_LANG -> selectedLearningLanguage != null
        QuestionStep.FEEDBACK_LANG -> selectedFeedbackLanguage != null
        QuestionStep.ENDER -> true
    }

    val progress by animateFloatAsState(
        targetValue = when (currentStep) {
            QuestionStep.LEARNING_LANG -> 0.5f
            QuestionStep.FEEDBACK_LANG -> 1.0f
            else -> 0.0f
        },
        label = "ProgressAnimation"
    )

    val showProgressHeader = when(currentStep) {
        QuestionStep.LEARNING_LANG -> true
        QuestionStep.FEEDBACK_LANG -> true
        else -> false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (showProgressHeader) {
            ProgressHeader(progress) {
                if (currentStep == QuestionStep.FEEDBACK_LANG) currentStep =
                    QuestionStep.LEARNING_LANG
            }
        }

        AnimatedStepContent(
            targetState = currentStep,
            modifier = Modifier.weight(1f)
        ) { step ->
            when (step) {
                QuestionStep.STARTER -> QuestionsStarter()
                QuestionStep.LEARNING_LANG -> {
                    AskLearningLangScreen(
                        selectedLanguage = selectedLearningLanguage,
                        onLanguageSelected = { lang -> onLearningLanguageSelected(lang) }
                    )
                }
                QuestionStep.FEEDBACK_LANG -> {
                    AskFeedbackLangScreen(
                        selectedLanguage = selectedFeedbackLanguage,
                        onLanguageSelected = { lang -> onFeedbackLanguageSelected(lang) }
                    )
                }
                QuestionStep.ENDER -> QuestionsEnder()
            }
        }

        PrimaryButton(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            enabled = enableButton,
            text = "Continue",
            onClick = {
                when (currentStep) {
                    QuestionStep.STARTER -> currentStep = QuestionStep.LEARNING_LANG
                    QuestionStep.LEARNING_LANG -> currentStep = QuestionStep.FEEDBACK_LANG
                    QuestionStep.FEEDBACK_LANG -> {
                        currentStep = QuestionStep.ENDER
                    }
                    QuestionStep.ENDER -> onFinish()
                }
            }
        )
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
fun QuestionFlowManagerPreview() {
    var learning by remember { mutableStateOf<Language?>(null) }
    var feedback by remember { mutableStateOf<Language?>(null) }

    QuestionFlowManagerContent(
        selectedLearningLanguage = learning,
        selectedFeedbackLanguage = feedback,
        onLearningLanguageSelected = { learning = it },
        onFeedbackLanguageSelected = { feedback = it },
        onFinish = { }
    )
}