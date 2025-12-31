package com.masterproject.englishapp.user

import androidx.compose.runtime.currentRecomposeScope
import com.google.firebase.auth.FirebaseAuth
import com.masterproject.englishapp.auth.AuthService
import com.masterproject.englishapp.auth.firebase.FirebaseAuthService
import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.learning.bkt.BKTKnowledgeModel
import com.masterproject.englishapp.learning.core.KnowledgeModel

class UserContext(
    private val authService: AuthService
) {
    var currentUser: UserModel? = null

    val model: KnowledgeModel?
        get() = currentUser?.model

    val learningLanguage: Language
        get() = currentUser?.learningLanguage ?: Language.EN

    val feedbackLanguage: Language
        get() = currentUser?.feedbackLanguage ?: Language.EN

    suspend fun authenticate(email: String, password: String): String {
        return authService.login(email, password)
    }

    suspend fun signUp(email: String, password: String): String {
        return authService.signUp(email, password)
    }

    fun setUser(user: UserModel) {
        this.currentUser = user
    }

    fun logout() {
        authService.logout()
        currentUser = null
    }

    fun isLoggedIn(): Boolean {
        return currentUser != null || authService.getCurrentUserId() != null
    }

    fun getCurrentUid(): String? {
        return currentUser?.id ?: authService.getCurrentUserId()
    }
}