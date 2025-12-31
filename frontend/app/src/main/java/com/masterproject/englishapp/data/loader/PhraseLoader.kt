package com.masterproject.englishapp.data.loader

import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.data.phrase.Phrase

class PhraseLoader(
    private val assetLoader: AssetLoader
) {

    private val cache = mutableMapOf<String, List<Phrase>>()

    fun load(
        language: Language
    ): List<Phrase> {

        val path = buildPath(language)

        cache[path]?.let { return it }

        val jsonText = assetLoader.loadText(path)
        val json = parseJson<Map<String, String>>(jsonText)

        val phrases = json.map { (id, text) ->
            Phrase(
                id = id,
                text = text,
                language = language,
                isQuestion = isQuestion(text)
            )
        }

        cache[path] = phrases
        return phrases
    }

    private fun isQuestion(text: String): Boolean =
        text.trim().endsWith("?")

    private fun buildPath(
        language: Language
    ): String =
        "data/phrases/${language.name.lowercase()}.json"
}