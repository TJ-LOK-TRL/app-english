package com.masterproject.englishapp.exercises

import com.masterproject.englishapp.grammar.Language
import com.masterproject.englishapp.data.loader.PhraseLoader
import com.masterproject.englishapp.data.phrase.Phrase
import com.masterproject.englishapp.exercises.model.Exercise
import com.masterproject.englishapp.result.AppError
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.selector.Selector
import kotlin.collections.filter
import kotlin.collections.firstOrNull
import kotlin.collections.ifEmpty
import kotlin.collections.random

class OrderSentence(
    val phraseLoader: PhraseLoader
) : Exercise() {
    fun getData(
        learningLanguage: Language,
        feedbackLanguage: Language,
        phraseSelector: Selector<Phrase>
    ): AppResult<OrderSentenceData> {

        val learningPhrases = phraseLoader.load(learningLanguage)
            .ifEmpty { return AppResult.Error(AppError.EmptyData) }

        val learningPhrase = phraseSelector.select(learningPhrases, count = 1)
            .first()

        val feedbackPhrases = phraseLoader.load(feedbackLanguage)
            .ifEmpty { return AppResult.Error(AppError.EmptyData) }

        val feedbackPhrase = feedbackPhrases.firstOrNull { it.id == learningPhrase.id }
            ?: return AppResult.Error(AppError.Custom("Falta tradução para '${learningPhrase.id}'"))

        val correctOrder = learningPhrase.text
            .trim()
            .split(Regex("\\s+"))

        if (correctOrder.size < 2) {
            return AppResult.Error(
                AppError.Custom("Frase demasiado curta para Order Sentence")
            )
        }

        val shuffledWords = correctOrder.shuffled()

        val data = OrderSentenceData(
            learningPhrase = learningPhrase,
            shuffledWords = shuffledWords,
            correctOrder = correctOrder,
            feedbackPhrase = feedbackPhrase
        )

        return AppResult.Success(data)
    }
}

data class OrderSentenceData(
    val learningPhrase: Phrase,
    val shuffledWords: List<String>,
    val correctOrder: List<String>,
    val feedbackPhrase: Phrase
)