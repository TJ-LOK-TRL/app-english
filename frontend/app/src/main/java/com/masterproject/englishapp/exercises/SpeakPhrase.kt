package com.masterproject.englishapp.exercises

import com.masterproject.englishapp.grammar.Language
import com.masterproject.englishapp.data.loader.PhraseLoader
import com.masterproject.englishapp.data.phrase.Phrase
import com.masterproject.englishapp.exercises.model.Exercise
import com.masterproject.englishapp.recorder.AudioRecorder
import com.masterproject.englishapp.result.AppError
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.selector.Selector

class SpeakPhrase(
    private val phraseLoader: PhraseLoader
) : Exercise() {

    fun getData(
        learningLanguage: Language,
        feedbackLanguage: Language,
        phraseSelector: Selector<Phrase>,
        recorder: AudioRecorder
    ): AppResult<SpeakPhraseData> {

        val pool = phraseLoader.load(learningLanguage)
            .ifEmpty { return AppResult.Error(AppError.EmptyData) }

        val learningPhrase = phraseSelector.select(pool, count = 1).firstOrNull()
            ?: return AppResult.Error(AppError.EmptyData)

        val feedbackPhrases = phraseLoader.load(feedbackLanguage)
        val feedbackPhrase = feedbackPhrases.firstOrNull { it.id == learningPhrase.id }
            ?: return AppResult.Error(AppError.Custom("Falta tradução"))

        return AppResult.Success(
            SpeakPhraseData(
                learningPhrase = learningPhrase.text,
                feedbackPhrase = feedbackPhrase.text,
                recorder = recorder
            )
        )
    }
}

data class SpeakPhraseData(
    val learningPhrase: String,
    val feedbackPhrase: String,
    val recorder: AudioRecorder
)