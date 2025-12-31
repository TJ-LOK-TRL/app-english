package com.masterproject.englishapp.auth.firebase

import com.google.firebase.auth.FirebaseAuth
import com.masterproject.englishapp.auth.AuthService
import kotlinx.coroutines.tasks.await

/**
 * Handles authentication using Firebase Auth.
 */
class FirebaseAuthService(
    private val firebaseAuth: FirebaseAuth
) : AuthService {

    override suspend fun signUp(email: String, password: String): String {
        val result = firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .await()

        return result.user?.uid
            ?: throw IllegalStateException("User UID is null after registration")
    }

    override suspend fun login(email: String, password: String): String {
        val result = firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .await()

        return result.user?.uid
            ?: throw IllegalStateException("User UID is null")
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUserId(): String? =
        firebaseAuth.currentUser?.uid
}