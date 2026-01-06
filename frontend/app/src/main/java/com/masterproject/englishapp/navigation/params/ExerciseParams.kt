package com.masterproject.englishapp.navigation.params

import com.masterproject.englishapp.grammar.Category
import com.masterproject.englishapp.exercises.model.ExerciseType

data class ExerciseParams(
    val exerciseTypes: Set<ExerciseType> = emptySet(),
    val categories: Set<Category> = emptySet(),
) {
    constructor(
        exerciseType: ExerciseType? = null,
        category: Category? = null,
    ) : this(
        exerciseTypes = exerciseType?.let { setOf(it) } ?: emptySet(),
        categories = category?.let { setOf(it) } ?: emptySet(),
    )


    fun toQuery(): String {
        val typesParam = exerciseTypes.joinToString(",") { it.name.lowercase() }
        val categoriesParam = categories.joinToString(",") { it.name.lowercase() }

        return buildString {
            if (typesParam.isNotEmpty()) {
                append("?types=$typesParam")
                if (categoriesParam.isNotEmpty()) {
                    append("&categories=$categoriesParam")
                }
            } else if (categoriesParam.isNotEmpty()) {
                append("?categories=$categoriesParam")
            } else {
                // Both empty so return empty string
                return ""
            }
        }
    }

    companion object {
        fun parseQuery(
            types: String?,
            categories: String?,
        ): ExerciseParams {
            val typeSet: Set<ExerciseType> = types
                ?.split(",")
                ?.mapNotNull { runCatching { ExerciseType.valueOf(it.uppercase()) }.getOrNull() }
                ?.toSet()
                ?: emptySet()

            val categorySet: Set<Category> = categories
                ?.split(",")
                ?.mapNotNull { runCatching { Category.valueOf(it.uppercase()) }.getOrNull() }
                ?.toSet()
                ?: emptySet()

            return ExerciseParams(
                exerciseTypes = typeSet,
                categories = categorySet,
            )
        }
    }
}
