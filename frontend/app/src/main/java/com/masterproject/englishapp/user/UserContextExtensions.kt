package com.masterproject.englishapp.user

import android.util.Log
import com.masterproject.englishapp.learning.blind.BlindKnowledgeModel
import com.masterproject.englishapp.learning.core.KnowledgeModel

fun UserContext.modelOrBlind(logTag: String = "UserContext"): KnowledgeModel {
    return this.model ?: run {
        Log.w(logTag, "User model is null, using BlindModel fallback")
        BlindKnowledgeModel()
    }
}