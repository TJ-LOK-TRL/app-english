package com.masterproject.englishapp.exercises.model

import androidx.compose.runtime.Composable
import com.masterproject.englishapp.exercises.BoolMeaningData
import com.masterproject.englishapp.exercises.OrderSentenceData
import com.masterproject.englishapp.exercises.SelectCorrectWordData
import com.masterproject.englishapp.exercises.SoundQuizData
import com.masterproject.englishapp.exercises.SoundQuizPhraseData
import com.masterproject.englishapp.exercises.SpeakPhraseData
import com.masterproject.englishapp.exercises.SpeakTokenData
import com.masterproject.englishapp.exercises.base.ExerciseBaseWrapper
import com.masterproject.englishapp.exercises.base.ExerciseInfo
import com.masterproject.englishapp.screens.exercises.boolmeaning.BoolMeaningContent
import com.masterproject.englishapp.screens.exercises.boolmeaning.BoolMeaningViewModel
import com.masterproject.englishapp.screens.exercises.ordersentence.OrderSentenceContent
import com.masterproject.englishapp.screens.exercises.ordersentence.OrderSentenceViewModel
import com.masterproject.englishapp.screens.exercises.selectcorrectword.SelectCorrectWordContent
import com.masterproject.englishapp.screens.exercises.selectcorrectword.SelectCorrectWordViewModel
import com.masterproject.englishapp.screens.exercises.soundquiz.SoundQuizContent
import com.masterproject.englishapp.screens.exercises.soundquiz.SoundQuizViewModel
import com.masterproject.englishapp.screens.exercises.soundquizphrase.SoundQuizPhraseContent
import com.masterproject.englishapp.screens.exercises.soundquizphrase.SoundQuizPhraseViewModel
import com.masterproject.englishapp.screens.exercises.speakphrase.SpeakPhraseContent
import com.masterproject.englishapp.screens.exercises.speakphrase.SpeakPhraseViewModel
import com.masterproject.englishapp.screens.exercises.speakword.SpeakWordContent
import com.masterproject.englishapp.screens.exercises.speakword.SpeakWordViewModel

enum class ExerciseKind(
    val supportedTypes: Set<ExerciseType>,
    val screen: @Composable (
        info: ExerciseInfo,
        onResult: (ExerciseResult) -> Unit,
    ) -> Unit
) {
    SOUND_QUIZ(
        supportedTypes = setOf(ExerciseType.LISTENING),
        screen = { info, onResult ->
            ExerciseBaseWrapper<SoundQuizData, SoundQuizViewModel>(exerciseInfo = info, onResult = onResult) { data ->
                SoundQuizContent(data, onResult)
            }
        }
    ),

    BOOL_MEANING(
        supportedTypes = setOf(ExerciseType.COMPREHENSION),
        screen = { info, onResult ->
            ExerciseBaseWrapper<BoolMeaningData, BoolMeaningViewModel>(exerciseInfo = info, onResult = onResult) { data ->
                BoolMeaningContent(data, onResult)
            }
        }
    ),

    ORDER_SENTENCE(
        supportedTypes = setOf(ExerciseType.WRITE),
        screen = { info, onResult ->
            ExerciseBaseWrapper<OrderSentenceData, OrderSentenceViewModel>(exerciseInfo = info, onResult = onResult) { data ->
                OrderSentenceContent(data, onResult)
            }
        }
    ),

    SELECT_CORRECT_WORD(
        supportedTypes = setOf(ExerciseType.WRITE, ExerciseType.COMPREHENSION),
        screen = { info, onResult ->
            ExerciseBaseWrapper<SelectCorrectWordData, SelectCorrectWordViewModel>(exerciseInfo = info, onResult = onResult) { data ->
                SelectCorrectWordContent(data, onResult)
            }
        }
    ),

    SOUND_QUIZ_PHRASE(
        supportedTypes = setOf(ExerciseType.LISTENING),
        screen = { info, onResult ->
            ExerciseBaseWrapper<SoundQuizPhraseData, SoundQuizPhraseViewModel>(exerciseInfo = info, onResult = onResult) { data ->
                SoundQuizPhraseContent(data, onResult)
            }
        }
    ),

    SPEAK_PHRASE(
        supportedTypes = setOf(ExerciseType.SPEAK),
        screen = { info, onResult ->
            ExerciseBaseWrapper<SpeakPhraseData, SpeakPhraseViewModel>(exerciseInfo = info, onResult = onResult) { data ->
                SpeakPhraseContent(data, onResult)
            }
        }
    ),

    SPEAK_WORD(
        supportedTypes = setOf(ExerciseType.SPEAK),
        screen = { info, onResult ->
            ExerciseBaseWrapper<SpeakTokenData, SpeakWordViewModel>(exerciseInfo = info, onResult = onResult) { data ->
                SpeakWordContent(data, onResult)
            }
        }
    )
}