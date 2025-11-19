package com.masterproject.englishapp.permissions

import android.Manifest

/**
 * Centralized enum of app permissions with their rationale messages.
 */
enum class AppPermission(
    val systemName: String,
    val rationale: String
) {
    RECORD_AUDIO(
        Manifest.permission.RECORD_AUDIO,
        "Audio recording is essential for voice practice and pronunciation feedback."
    ),
    CAMERA(
        Manifest.permission.CAMERA,
        "Camera access enables object detection features for interactive learning."
    );

    companion object {
        /** Helper to find enum by permission string */
        fun fromSystemName(name: String): AppPermission? =
            entries.find { it.systemName == name }
    }
}