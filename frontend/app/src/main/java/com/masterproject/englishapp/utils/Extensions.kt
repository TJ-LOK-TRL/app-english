package com.masterproject.englishapp.utils

import android.database.sqlite.SQLiteException
import com.masterproject.englishapp.data.phrase.Phrase
import com.masterproject.englishapp.result.AppError
import java.io.IOException

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

// Maybe start using this or not
fun Throwable.toAppError(): AppError {
    return when (this) {
        is IOException -> AppError.Network
        else -> AppError.Unknown(this)
    }
}