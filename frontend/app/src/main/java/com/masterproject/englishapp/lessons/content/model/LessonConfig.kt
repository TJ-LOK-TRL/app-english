package com.masterproject.englishapp.lessons.content.model

import com.masterproject.englishapp.data.providers.ProviderSource
import com.masterproject.englishapp.grammar.Category

data class LessonConfig(
    val context: String,
    val type: LessonType,
    val sources: Set<ProviderSource>,
    val categories: List<Category> = emptyList()
)
