package com.masterproject.englishapp.data.loader

import com.masterproject.englishapp.grammar.Category
import com.masterproject.englishapp.grammar.Language
import com.masterproject.englishapp.data.token.AnyToken
import com.masterproject.englishapp.data.token.Token
import com.masterproject.englishapp.data.token.TokenId
import com.masterproject.englishapp.grammar.GClass
import com.masterproject.englishapp.grammar.AdjectiveValue
import com.masterproject.englishapp.grammar.Gender
import com.masterproject.englishapp.grammar.GNumber
import com.masterproject.englishapp.grammar.NounValue
import com.masterproject.englishapp.grammar.WordValue

class TokenLoader(
    @PublishedApi internal val assetLoader: AssetLoader
) {

    @PublishedApi internal val cache = mutableMapOf<String, List<AnyToken>>()

    /**
     * T é o tipo específico (NounValue, VerbValue, etc.)
     * Usamos reified para podermos verificar o tipo em tempo de execução
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : WordValue> extractSingle(
        grammarClass: GClass,
        category: Category,
        language: Language
    ): List<Token<T>> {

        val path = buildPath(grammarClass, category, language)

        // Se estiver no cache, fazemos o cast seguro para o tipo pedido
        cache[path]?.let { return it as List<Token<T>> }

        val jsonText = assetLoader.loadText(path)
        val json = parseJson<Map<String, TokenJsonEntry>>(jsonText)

        val tokens = json.map { (localId, entry) ->
            Token(
                id = TokenId(grammarClass, category, localId),
                grammarClass = grammarClass,
                language = language,
                values = entry.values.split().let { texts ->
                    texts.mapIndexed { idx, text ->
                        createWordValue(grammarClass, idx, text, texts.size) as T
                    }
                },
                metadata = mapOf(
                    "example" to entry.example
                )
            )
        }

        cache[path] = tokens
        return tokens
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : WordValue> extract(
        grammarClasses: Set<GClass>,
        categories: Set<Category>,
        languages: Set<Language>
    ): List<Token<T>> {

        val gcs = grammarClasses.ifEmpty { GClass.entries }
        val cats = categories.ifEmpty { Category.entries }
        val langs = languages.ifEmpty { Language.entries }

        return buildList {
            for (gc in gcs) {
                for (cat in cats) {
                    for (lang in langs) {
                        addAll(extractSingle<T>(gc, cat, lang))
                    }
                }
            }
        }
    }

    @PublishedApi
    internal fun buildPath(
        grammarClass: GClass,
        category: Category,
        language: Language
    ): String = "data/tokens/${grammarClass.folderName()}/${category.name.lowercase()}/${language.name.lowercase()}.json"

    @PublishedApi
    internal fun resolveGenderNumber(
        idx: Int,
        total: Int
    ): Pair<Gender, GNumber> =
        when (total) {
            1 -> Gender.NEUTRAL to GNumber.NEUTRAL

            2 -> Gender.NEUTRAL to when (idx) {
                0 -> GNumber.SINGULAR
                1 -> GNumber.PLURAL
                else -> error("Invalid index=$idx for total=$total")
            }

            4 -> when (idx) {
                0 -> Gender.MASCULINE to GNumber.SINGULAR
                1 -> Gender.MASCULINE to GNumber.PLURAL
                2 -> Gender.FEMININE to GNumber.SINGULAR
                3 -> Gender.FEMININE to GNumber.PLURAL
                else -> error("Invalid index=$idx for total=$total")
            }

            else -> error(
                "Invalid values count=$total. Expected 1, 2 or 4."
            )
        }

    // Esta função centraliza a criação de cada tipo
    @PublishedApi
    internal fun createWordValue(
        grammarClass: GClass,
        idx: Int,
        text: String,
        total: Int
    ): WordValue = when (grammarClass) {

        GClass.NOUN -> {
            val (gender, number) = resolveGenderNumber(idx, total)
            NounValue(text, gender, number)
        }

        GClass.ADJECTIVE -> {
            val (gender, number) = resolveGenderNumber(idx, total)
            AdjectiveValue(text, gender, number)
        }

        GClass.VERB ->
            throw NotImplementedError(
                "Verb token loading is not implemented yet"
            )
    }
}