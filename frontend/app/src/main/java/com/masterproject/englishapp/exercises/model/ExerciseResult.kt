package com.masterproject.englishapp.exercises.model

sealed class ExerciseResult(
    val message: String?
) {
    data class Correct(val msg: String? = null) : ExerciseResult(msg)
    data class Wrong(val msg: String? = null) : ExerciseResult(msg)
    data class Skipped(val msg: String? = null) : ExerciseResult(msg)

    companion object {
        fun fromBool(isCorrect: Boolean): ExerciseResult =
            if (isCorrect) Correct() else Wrong()
    }
}