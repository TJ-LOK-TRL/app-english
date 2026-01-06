package com.masterproject.englishapp.utils

import androidx.navigation.NavOptionsBuilder
import com.google.ai.client.generativeai.type.content
import com.masterproject.englishapp.grammar.Language
import com.masterproject.englishapp.learning.blind.BlindKnowledgeModel
import com.masterproject.englishapp.lessons.content.meaninglesson.MeaningLessonData
import com.masterproject.englishapp.navigation.NavigationActions
import com.masterproject.englishapp.navigation.Screen
import com.masterproject.englishapp.recorder.AudioRecorder
import com.masterproject.englishapp.user.UserModel
import com.masterproject.englishapp.user.UserPreferences

val DummyNavigator = object : NavigationActions {
    override fun navigate(
        screen: Screen,
        params: String?,
        navOptions: NavOptionsBuilder.() -> Unit
    ) { }

    override fun navigateUp(
        fallbackScreen: Screen?,
        navOptions: NavOptionsBuilder.() -> Unit
    ) { }
}

val DummyAudioRecorder = object : AudioRecorder {
    override fun startRecording(onAudioData: (FloatArray) -> Unit) { }

    override fun stopRecording(): FloatArray? {
        return null
    }

    override val isRecording: Boolean
        get() = false
}

val DummyUserPreferences = UserPreferences(
    learningLanguage = Language.EN,
    feedbackLanguage = Language.PT,
    notificationsEnabled = true,
    dailyGoalMinutes = 15
)

val DummyUserModel = UserModel(
    id = "",
    name = "Maria Chaves",
    email = "example@gmail.pt",
    preferences = DummyUserPreferences,
    model = BlindKnowledgeModel()
)

val DummyMeaningLessonData = MeaningLessonData(
    contextTitle = "Airport",
    learningPhrase = "Avião",
    feedbackPhrase = "Plane",
    explanation = "A vehicle that flies and carries passengers."
)