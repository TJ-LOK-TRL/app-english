package com.masterproject.englishapp.data.user.entities

data class UserEntity(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val preferences: UserPreferencesEntity = UserPreferencesEntity(),
    val knowledge: KnowledgeStateEntity = KnowledgeStateEntity()
)