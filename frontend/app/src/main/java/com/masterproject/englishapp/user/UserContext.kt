package com.masterproject.englishapp.user

import androidx.compose.runtime.currentRecomposeScope
import com.google.firebase.auth.FirebaseAuth
import com.masterproject.englishapp.auth.AuthService
import com.masterproject.englishapp.auth.firebase.FirebaseAuthService
import com.masterproject.englishapp.data.Language
import com.masterproject.englishapp.data.user.UserRepository
import com.masterproject.englishapp.data.user.mapper.toEntity
import com.masterproject.englishapp.learning.bkt.BKTKnowledgeModel
import com.masterproject.englishapp.learning.core.KnowledgeModel

class UserContext(
    private val authService: AuthService,
    private val userPreferencesStore: UserPreferencesStore,
    private val userRepository: UserRepository
) {
    var currentUser: UserModel? = null

    val model: KnowledgeModel?
        get() = currentUser?.model

    val learningLanguage: Language
        get() = currentUser?.preferences?.learningLanguage ?: Language.EN

    val feedbackLanguage: Language
        get() = currentUser?.preferences?.feedbackLanguage ?: Language.EN

    suspend fun authenticate(email: String, password: String): String {
        return authService.login(email, password)
    }

    suspend fun signUp(email: String, password: String): String {
        return authService.signUp(email, password)
    }

    suspend fun saveProgress() {
        try {
            val entity = currentUser?.toEntity() ?: return
            val uid = getCurrentUid() ?: return

            userRepository.updateUser(uid, entity)

            android.util.Log.d("USER_CONTEXT", "Progress saved with success for the UID: $uid")
        } catch (e: Exception) {
            android.util.Log.e("USER_CONTEXT", "Error saving progress", e)
        }
    }

    fun setUser(user: UserModel) {
        this.currentUser = user
        userPreferencesStore.loadFromDomain(user.preferences)
    }

    fun logout() {
        authService.logout()
        currentUser = null
        userPreferencesStore.resetToDefaults() // Maybe not neeeded
    }

    fun isLoggedIn(): Boolean {
        return currentUser != null || authService.getCurrentUserId() != null
    }

    fun getCurrentUid(): String? {
        return currentUser?.id ?: authService.getCurrentUserId()
    }
}