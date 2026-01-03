package com.masterproject.englishapp.utils

import androidx.navigation.NavOptionsBuilder
import com.masterproject.englishapp.navigation.NavigationActions
import com.masterproject.englishapp.navigation.Screen
import com.masterproject.englishapp.recorder.AudioRecorder

val DummyNavigator = object : NavigationActions {
    override fun navigate(
        screen: Screen,
        params: String?,
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