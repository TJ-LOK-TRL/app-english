package com.masterproject.englishapp.exercises

import com.masterproject.englishapp.grammar.Language
import com.masterproject.englishapp.data.loader.PhraseLoader
import com.masterproject.englishapp.data.phrase.Phrase
import com.masterproject.englishapp.exercises.model.Exercise
import com.masterproject.englishapp.result.AppError
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.selector.Selector

class BoolMeaning(
    private val phraseLoader: PhraseLoader,
) : Exercise() {

    fun getData(
        learningLanguage: Language,
        feedbackLanguage: Language,
        phraseSelector: Selector<Phrase>
    ): AppResult<BoolMeaningData> {

        val learningPhrases = phraseLoader.load(learningLanguage)
            .ifEmpty { return AppResult.Error(AppError.EmptyData) }

        val learningPhrase = phraseSelector.select(learningPhrases, count = 1)
            .first()

        val feedbackPhrases = phraseLoader.load(feedbackLanguage)
            .ifEmpty { return AppResult.Error(AppError.EmptyData) }

        val isTrue = (0..1).random() == 0
        val feedbackPhrase =
            if (isTrue) {
                feedbackPhrases.firstOrNull { it.id == learningPhrase.id }
                    ?: return AppResult.Error(
                        AppError.Custom("Falta tradução para '${learningPhrase.id}'")
                    )
            } else {
                feedbackPhrases.filter { it.id != learningPhrase.id }.random()
            }


        val data = BoolMeaningData(
            learningPhrase = learningPhrase,
            feedbackPhrase = feedbackPhrase,
            isCorrectAnswer = isTrue
        )

        return AppResult.Success(data)
    }
}

data class BoolMeaningData(
    val learningPhrase: Phrase,
    val feedbackPhrase: Phrase,
    val isCorrectAnswer: Boolean
)