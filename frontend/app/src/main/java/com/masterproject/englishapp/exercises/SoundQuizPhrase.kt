package com.masterproject.englishapp.exercises

import com.masterproject.englishapp.grammar.Language
import com.masterproject.englishapp.data.loader.PhraseLoader
import com.masterproject.englishapp.data.phrase.Phrase
import com.masterproject.englishapp.exercises.model.Exercise
import com.masterproject.englishapp.network.ApiService
import com.masterproject.englishapp.network.safeApiCall
import com.masterproject.englishapp.result.AppError
import com.masterproject.englishapp.result.AppResult
import com.masterproject.englishapp.result.getOrReturn
import com.masterproject.englishapp.selector.Selector
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class SoundQuizPhrase(
    private val phraseLoader: PhraseLoader,
    private val api: ApiService
) : Exercise() {

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun getData(
        learningLanguage: Language,
        phraseSelector: Selector<Phrase>
    ): AppResult<SoundQuizPhraseData> {

        // 1. Carregar todas as frases no idioma de aprendizagem
        val pool = phraseLoader.load(learningLanguage)
            .ifEmpty { return AppResult.Error(AppError.EmptyData) }

        if (pool.size < 4) return AppResult.Error(AppError.Custom("Pool de frases insuficiente"))

        // 2. Selecionar a frase correta (a que será ouvida)
        val correctPhrase = phraseSelector.select(pool, count = 1)
            .firstOrNull() ?: return AppResult.Error(AppError.EmptyData)

        // 3. Selecionar 3 frases erradas (distratores)
        val wrongPhrases = pool
            .filter { it.id != correctPhrase.id }
            .shuffled()
            .take(3)

        // 4. Sintetizar o áudio da frase correta
        // Usamos o texto integral da frase para o TTS
        val synthResult = safeApiCall { api.synthesize(correctPhrase.text) }
            .getOrReturn { return AppResult.Error(it) }

        val audioBytes = Base64.decode(synthResult.audio)

        return AppResult.Success(
            SoundQuizPhraseData(
                audio = audioBytes,
                sampleRate = synthResult.sampleRate,
                correctPhrase = correctPhrase,
                wrongPhrases = wrongPhrases
            )
        )
    }
}

data class SoundQuizPhraseData(
    val audio: ByteArray,
    val sampleRate: Int,
    val correctPhrase: Phrase,
    val wrongPhrases: List<Phrase>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SoundQuizPhraseData

        if (sampleRate != other.sampleRate) return false
        if (!audio.contentEquals(other.audio)) return false
        if (correctPhrase != other.correctPhrase) return false
        if (wrongPhrases != other.wrongPhrases) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sampleRate
        result = 31 * result + audio.contentHashCode()
        result = 31 * result + correctPhrase.hashCode()
        result = 31 * result + wrongPhrases.hashCode()
        return result
    }
}