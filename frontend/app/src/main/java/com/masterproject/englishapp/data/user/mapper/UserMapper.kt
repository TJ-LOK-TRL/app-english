package com.masterproject.englishapp.data.user.mapper

import com.masterproject.englishapp.grammar.Language
import com.masterproject.englishapp.data.user.entities.KnowledgeStateEntity
import com.masterproject.englishapp.data.user.entities.UserEntity
import com.masterproject.englishapp.data.user.entities.UserPreferencesEntity
import com.masterproject.englishapp.learning.bkt.BKTKnowledgeModel
import com.masterproject.englishapp.learning.core.KnowledgeModel
import com.masterproject.englishapp.learning.core.SkillKey
import com.masterproject.englishapp.user.UserModel
import com.masterproject.englishapp.user.UserPreferences

fun UserEntity.toDomain(): UserModel =
    UserModel(
        id = id,
        name = name,
        email = email,
        preferences = preferences.toDomain(),
        model = BKTKnowledgeModel(
            initialState = knowledge.skills.mapKeys { SkillKey(it.key) }
        )
    )

fun UserModel.toEntity(): UserEntity =
    UserEntity(
        id = id,
        name = name,
        email = email,
        preferences = preferences.toEntity(),
        knowledge = model.toEntity()
    )

fun UserPreferencesEntity.toDomain(): UserPreferences =
    UserPreferences(
        learningLanguage = Language.valueOf(learningLanguage),
        feedbackLanguage = Language.valueOf(feedbackLanguage),
        notificationsEnabled = notificationsEnabled,
        dailyGoalMinutes = dailyGoalMinutes,
        soundEffectsEnabled = soundEffectsEnabled,
        vibrationEnabled = vibrationEnabled,
        dailyRemindersEnabled = dailyRemindersEnabled,
        gpsNotificationsEnabled = gpsNotificationsEnabled
    )

fun UserPreferences.toEntity(): UserPreferencesEntity =
    UserPreferencesEntity(
        learningLanguage = learningLanguage.name,
        feedbackLanguage = feedbackLanguage.name,
        notificationsEnabled = notificationsEnabled,
        dailyGoalMinutes = dailyGoalMinutes,
        soundEffectsEnabled = soundEffectsEnabled,
        vibrationEnabled = vibrationEnabled,
        dailyRemindersEnabled = dailyRemindersEnabled,
        gpsNotificationsEnabled = gpsNotificationsEnabled
    )

fun KnowledgeModel.toEntity(): KnowledgeStateEntity {
    val entityState = getState().mapKeys { it.key.value }
    return KnowledgeStateEntity(skills = entityState)
}

