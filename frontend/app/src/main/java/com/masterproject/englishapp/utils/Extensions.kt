package com.masterproject.englishapp.utils

import com.masterproject.englishapp.data.phrase.Phrase

fun Phrase.extractSkillIds(): List<String> {
    val fullPhrase = this.text

    val words = fullPhrase.lowercase()
        .replace(Regex("[^a-zA-Z ]"), "") // Remove pontuaction
        .split(" ")
        .filter { it.isNotBlank() }
        .distinct() // Avoid duplicate IDs if the phrase repeat words

    return (listOf(fullPhrase) + words).distinct()
}

// TODO: Remove this later when replace all String use cases to Phrase use
fun String.extractSkillIds(): List<String> {
    val text = this

    val words = text.lowercase()
        .replace(Regex("[^a-zA-Z ]"), "") // Remove pontuaction
        .split(" ")
        .filter { it.isNotBlank() }
        .distinct() // Avoid duplicate IDs if the phrase repeat words

    return (listOf(text) + words).distinct()
}