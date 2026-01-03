package com.masterproject.englishapp.data.user

import com.google.firebase.firestore.FirebaseFirestore
import com.masterproject.englishapp.data.user.entities.UserEntity
import kotlinx.coroutines.tasks.await

/**
 * Repository responsible for reading/writing user data from Firestore.
 */
class UserRepository(
    private val firestore: FirebaseFirestore
) {

    /**
     * Load a user from Firestore by UID.
     */
    suspend fun loadUser(uid: String): UserEntity {
        val snapshot = firestore
            .collection("users")
            .document(uid)
            .get()
            .await()  // requires kotlinx-coroutines-play-services

        return snapshot.toObject(UserEntity::class.java)
            ?: throw IllegalStateException("User not found in Firestore")
    }


    suspend fun createUser(uid: String, user: UserEntity) {
        firestore.collection("users")
            .document(uid)
            .set(user)
            .await()
    }

    suspend fun updateUser(uid: String, user: UserEntity) {
        firestore.collection("users")
            .document(uid)
            .set(user)
            .await()
    }
}