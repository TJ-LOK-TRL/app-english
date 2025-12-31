package com.masterproject.englishapp.navigation.params

import com.masterproject.englishapp.data.Category
import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.exercises.model.ExerciseType

data class ExerciseParams(
    val exerciseTypes: Set<ExerciseType> = emptySet(),
    val categories: Set<Category> = emptySet(),
    val language: Language = Language.EN
) {
    constructor(
        exerciseType: ExerciseType? = null,
        category: Category? = null,
        language: Language = Language.EN
    ) : this(
        exerciseTypes = exerciseType?.let { setOf(it) } ?: emptySet(),
        categories = category?.let { setOf(it) } ?: emptySet(),
        language = language
    )


    fun toQuery(): String {
        val typesParam = exerciseTypes.joinToString(",") { it.name.lowercase() }
        val categoriesParam = categories.joinToString(",") { it.name.lowercase() }
        return buildString {
            append("?language=${language.name.lowercase()}")
            if (typesParam.isNotEmpty()) append("&types=$typesParam")
            if (categoriesParam.isNotEmpty()) append("&categories=$categoriesParam")
        }
    }

    companion object {
        fun parseQuery(
            types: String?,
            categories: String?,
            language: String?
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

            val lang = runCatching { Language.valueOf(language?.uppercase() ?: "EN") }
                .getOrDefault(Language.EN)

            return ExerciseParams(
                exerciseTypes = typeSet,
                categories = categorySet,
                language = lang
            )
        }
    }
}
