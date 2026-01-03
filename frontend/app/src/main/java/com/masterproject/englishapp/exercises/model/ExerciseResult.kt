package com.masterproject.englishapp.exercises.model

sealed class ExerciseResult(
    open val skillIds: List<String> = emptyList(),
    open val message: String? = null
) {
    data class Correct(
        override val skillIds: List<String>,
        override val message: String? = null
    ) : ExerciseResult(skillIds = skillIds, message = message)

    data class Wrong(
        override val skillIds: List<String>,
        override val message: String? = null
    ) : ExerciseResult(skillIds = skillIds, message = message)

    data class Skipped(override val message: String? = null) : ExerciseResult(message = message)

    data class Error(override val message: String? = null) : ExerciseResult(message = message)

    companion object {
        fun fromBool(
            skillIds: List<String>,
            isCorrect: Boolean,
            correctMessage: String? = null,
            wrongMessage: String? = null
        ): ExerciseResult {
            return if (isCorrect) {
                Correct(skillIds = skillIds, message = correctMessage)
            } else {
                Wrong(skillIds = skillIds, message = wrongMessage)
            }
        }
    }
}