package com.masterproject.englishapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.masterproject.englishapp.auth.firebase.FirebaseAuthService
import com.masterproject.englishapp.data.user.UserRepository
import com.masterproject.englishapp.user.UserContext
import com.masterproject.englishapp.user.UserPreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserModule {
    @Provides
    @Singleton
    fun provideUserContext(
        authService: FirebaseAuthService,
        userPreferencesStore: UserPreferencesStore,
        userRepository: UserRepository
    ): UserContext = UserContext(authService, userPreferencesStore, userRepository)

    @Provides
    @Singleton
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository =
        UserRepository(firestore)
}