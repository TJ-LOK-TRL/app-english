package com.masterproject.englishapp.data.user

import com.google.firebase.firestore.FirebaseFirestore
import com.masterproject.englishapp.learning.core.KnowledgeModel
import com.masterproject.englishapp.user.UserModel
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
    suspend fun loadUser(uid: String): UserModel {
        val snapshot = firestore
            .collection("users")
            .document(uid)
            .get()
            .await()  // requires kotlinx-coroutines-play-services

        return snapshot.toObject(UserModel::class.java)
            ?: throw IllegalStateException("User not found in Firestore")
    }


    suspend fun createUser(uid: String, user: UserModel) {
        firestore.collection("users")
            .document(uid)
            .set(user)
            .await()
    }

    suspend fun loadKnowledgeModel(uid: String): KnowledgeModel {
        val snapshot = firestore
            .collection("users")
            .document(uid)
            .get()
            .await()

        return snapshot.toObject(UserModel::class.java)!!.model
    }
}