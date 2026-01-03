package com.masterproject.englishapp.exercises.pronunciation

import com.masterproject.englishapp.network.model.PronunciationResult

fun calculatePronunciationSuccess(
    result: PronunciationResult,
    minAverageScore: Float = -0.6f // Using Log instead of percentage, but percentage could also be with [-1.0f, 0.0f] to 0% to 100%
): Boolean {
    if (result.results.isEmpty()) return false

    val averageScore = result.results
        .map { it.score }
        .average()
        .toFloat()

    val hasFailedWord = result.results.any { it.label == "failed" }

    return averageScore >= minAverageScore && !hasFailedWord
}