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

class SelectCorrectWord(
    val phraseLoader: PhraseLoader
) : Exercise() {

    // Don't have time to put this in pretty json
    private val commonDistractors = listOf(
        "water", "house", "friend", "school", "night", "world", "family", "street",
        "money", "book", "food", "time", "place", "work", "life", "car", "day", "week"
    )

    fun getData(
        learningLanguage: Language,
        feedbackLanguage: Language,
        phraseSelector: Selector<Phrase>
    ): AppResult<SelectCorrectWordData> {

        val learningPhrases = phraseLoader.load(learningLanguage)
            .ifEmpty { return AppResult.Error(AppError.EmptyData) }

        val learningPhrase = phraseSelector.select(learningPhrases, count = 1).first()

        val feedbackPhrases = phraseLoader.load(feedbackLanguage)
            .ifEmpty { return AppResult.Error(AppError.EmptyData) }

        val feedbackPhrase = feedbackPhrases.firstOrNull { it.id == learningPhrase.id }
            ?: return AppResult.Error(AppError.Custom("Falta tradução"))

        val words = learningPhrase.text.trim().split(Regex("\\s+"))
        if (words.size < 3) return AppResult.Error(AppError.Custom("Frase curta"))

        val gapIndex = words.indices.random()

        val correctWord = words[gapIndex].replace(Regex("[^a-zA-Z]"), "")

        val wrongOptions = commonDistractors
            .filter { it.lowercase() != correctWord.lowercase() }
            .shuffled()
            .take(3)

        val allOptionsShuffled = (wrongOptions + correctWord).shuffled()

        val data = SelectCorrectWordData(
            learningPhrase = learningPhrase,
            words = words,
            gapIndex = gapIndex,
            correctOption = correctWord,
            allOptionsShuffled = allOptionsShuffled,
            feedbackPhrase = feedbackPhrase
        )

        return AppResult.Success(data)
    }
}

data class SelectCorrectWordData(
    val learningPhrase: Phrase,
    val words: List<String>,
    val gapIndex: Int,
    val correctOption: String,
    val allOptionsShuffled: List<String>,
    val feedbackPhrase: Phrase
)