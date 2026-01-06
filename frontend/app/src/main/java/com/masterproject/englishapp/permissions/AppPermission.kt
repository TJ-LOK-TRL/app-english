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
    ),
    WRITE_DISK(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        "Write access is required to save debug images and other files."
    ),
    READ_DISK(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        "Read access is required to load debug images and other files."
    ),
    LOCATION_COARSE(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        "Location access helps us provide context-aware learning based on your surroundings."
    ),
    LOCATION_FINE(
        Manifest.permission.ACCESS_FINE_LOCATION,
        "Precise location is needed to trigger geofencing for specific learning zones."
    ),
    LOCATION_BACKGROUND(
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        "Background location allows the app to alert you about learning opportunities even when your phone is in your pocket, ensuring a truly ubiquitous experience."
    );

    companion object {
        /** Helper to find enum by permission string */
        fun fromSystemName(name: String): AppPermission? =
            entries.find { it.systemName == name }
    }
}