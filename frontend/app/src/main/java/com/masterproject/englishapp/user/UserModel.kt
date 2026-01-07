package com.masterproject.englishapp.user

import com.masterproject.englishapp.grammar.Language
import com.masterproject.englishapp.learning.bkt.BKTKnowledgeModel
import com.masterproject.englishapp.learning.core.KnowledgeModel

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val preferences: UserPreferences,
    val model: KnowledgeModel,
    val statistics: UserStatistics = UserStatistics()
)