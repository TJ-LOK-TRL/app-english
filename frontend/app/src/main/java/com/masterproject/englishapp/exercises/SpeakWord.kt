package com.masterproject.englishapp.exercises

import com.masterproject.englishapp.data.Category
import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.data.loader.TokenLoader
import com.masterproject.englishapp.data.token.AnyToken
import com.masterproject.englishapp.data.token.TokenId
import com.masterproject.englishapp.exercises.model.Exercise
import com.masterproject.englishapp.grammar.GClass
import com.masterproject.englishapp.grammar.WordValue
import com.masterproject.englishapp.recorder.AudioRecorder
import com.masterproject.englishapp.result.AppError
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.selector.Selector

class SpeakWord(
    private val tokenLoader: TokenLoader
) : Exercise() {

    fun getData(
        grammarClasses: Set<GClass> = emptySet(),
        categories: Set<Category> = emptySet(),
        learningLanguage: Language,
        feedbackLanguage: Language,
        tokenSelector: Selector<AnyToken>,
        recorder: AudioRecorder
    ): AppResult<SpeakTokenData> {

        val fullLearningPool = tokenLoader.extract<WordValue>(
            grammarClasses, categories, setOf(learningLanguage)
        )

        if (fullLearningPool.isEmpty()) return AppResult.Error(AppError.EmptyData)

        // Select target token
        val learningToken = tokenSelector.select(fullLearningPool, 1).firstOrNull()
            ?: return AppResult.Error(AppError.EmptyData)

        // Choose text
        val textToSpeak = learningToken.values.random().text

        return AppResult.Success(
            SpeakTokenData(
                tokenId = learningToken.id,
                learningWord = textToSpeak,
                feedbackWord = textToSpeak,
                recorder = recorder
            )
        )
    }
}

data class SpeakTokenData(
    val tokenId: TokenId,
    val learningWord: String,
    val feedbackWord: String,
    val recorder: AudioRecorder
)