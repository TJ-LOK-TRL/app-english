package com.masterproject.englishapp.grammar

sealed class WordValue {
    abstract val text: String
}

data class NounValue(
    override val text: String,
    val gender: Gender? = null,
    val number: GNumber? = null
) : WordValue()

data class VerbValue(
    override val text: String,
    val tense: Tense,
    val person: Person,
    val gender: Gender? = null
) : WordValue()

data class AdjectiveValue(
    override val text: String,
    val gender: Gender? = null,
    val number: GNumber? = null
) : WordValue()