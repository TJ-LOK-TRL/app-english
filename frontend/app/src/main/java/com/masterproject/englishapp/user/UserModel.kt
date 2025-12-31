package com.masterproject.englishapp.user

import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.learning.bkt.BKTKnowledgeModel
import com.masterproject.englishapp.learning.core.KnowledgeModel

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val learningLanguage: Language,
    val feedbackLanguage: Language,
    val model: KnowledgeModel = BKTKnowledgeModel()
)