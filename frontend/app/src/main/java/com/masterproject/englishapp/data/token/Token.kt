package com.masterproject.englishapp.data.token

import com.masterproject.englishapp.data.Identifiable
import com.masterproject.englishapp.grammar.Language
import com.masterproject.englishapp.grammar.GClass
import com.masterproject.englishapp.grammar.WordValue

data class Token<out WV : WordValue>(
    override val id: TokenId,
    val grammarClass: GClass,
    val values: List<WV>,
    val language: Language,
    val metadata: Map<String, String> = emptyMap()
) : Identifiable {
    init {
        require(values.isNotEmpty()) { "Token must have at least one value" }
        require(values.all { grammarClass.valueType.isInstance(it) }) {
            "All values must match grammar class $grammarClass"
        }
    }
}

typealias AnyToken = Token<WordValue>