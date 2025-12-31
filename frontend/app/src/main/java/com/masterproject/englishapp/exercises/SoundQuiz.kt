package com.masterproject.englishapp.exercises

import com.masterproject.englishapp.data.Category
import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.data.loader.TokenLoader
import com.masterproject.englishapp.data.token.AnyToken
import com.masterproject.englishapp.exercises.model.Exercise
import com.masterproject.englishapp.grammar.GClass
import com.masterproject.englishapp.grammar.WordValue
import com.masterproject.englishapp.network.ApiService
import com.masterproject.englishapp.network.safeApiCall
import com.masterproject.englishapp.result.AppError
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.result.getOrReturn
import com.masterproject.englishapp.selector.Selector
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class SoundQuiz(
    private val tokenLoader: TokenLoader,
    private val api: ApiService
) : Exercise() {

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun getData(
        grammarClasses: Set<GClass> = emptySet(),
        categories: Set<Category> = emptySet(),
        languages: Set<Language> = emptySet(),
        rightTokenSelector: Selector<AnyToken>,
        wrongTokensSelector: Selector<AnyToken>
    ): AppResult<SoundQuizData> {

        // 1. Load the full token pool for the given constraints
        // TokenLoader cache guarantees this is fast after the first load
        val fullPool = tokenLoader.extract<WordValue>(
            grammarClasses,
            categories,
            languages
        )

        if (fullPool.isEmpty()) return AppResult.Error(AppError.EmptyData)

        // 2. Select the correct token
        val correctToken = rightTokenSelector
            .select(fullPool, 1)
            .firstOrNull() ?: return AppResult.Error(AppError.EmptyData)

        // 3. Build the pool for wrong options (exclude the correct token)
        val wrongPool = fullPool.filter { it.id != correctToken.id }

        // 4. Select wrong options using the provided strategy
        val wrongTokens = wrongTokensSelector
            .select(wrongPool, 3)
            .shuffled()

        // 5. Synthesize the audio for the correct word
        val tokenToSynthesize = correctToken.values.random()

        val synthResult = safeApiCall { api.synthesize(tokenToSynthesize.text) }
            .getOrReturn { return AppResult.Error(it) }

        val audioBytes = Base64.decode(synthResult.audio)

        val data = SoundQuizData(
            audio = audioBytes,
            sampleRate = synthResult.sampleRate,
            correctToken = correctToken,
            wrongTokens = wrongTokens
        )

        return AppResult.Success(data)
    }
}

data class SoundQuizData(
    val audio: ByteArray,
    val sampleRate: Int,
    val correctToken: AnyToken,
    val wrongTokens: List<AnyToken>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SoundQuizData

        if (sampleRate != other.sampleRate) return false
        if (!audio.contentEquals(other.audio)) return false
        if (correctToken != other.correctToken) return false
        if (wrongTokens != other.wrongTokens) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sampleRate
        result = 31 * result + audio.contentHashCode()
        result = 31 * result + correctToken.hashCode()
        result = 31 * result + wrongTokens.hashCode()
        return result
    }
}