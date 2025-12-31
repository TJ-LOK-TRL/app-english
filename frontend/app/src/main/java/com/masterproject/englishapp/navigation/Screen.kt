// navigation/Screen.kt
package com.masterproject.englishapp.navigation

import com.masterproject.englishapp.permissions.AppPermission
import com.masterproject.englishapp.permissions.plus

/**
 * Screens available in the English Learning Application
 *
 * @property route The display route for the screen
 * @property title The display title for the screen
 * @property requiredPermissions The required permissions for the screen
 */
enum class Screen(
    val route: String,
    val title: String,
    val requiredPermissions: List<AppPermission> = emptyList()
) {
    WELCOME("welcome", "Welcome"),
    INTRO("introduction", "Introduction"),
    HOME("home", "English Learning"),
    RECORDER("recorder", "Voice Recorder", AppPermission.RECORD_AUDIO),
    PRACTICE("practice", "Practice Exercises"),
    PROFILE("profile", "My Profile"),
    CHAT("chat", "AI Chat"),
    CAMERA("camera", "Object Camera", AppPermission.CAMERA),
    LOGIN("login", "Login");

    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route?.substringBefore("/")) {
                HOME.route -> HOME
                RECORDER.route -> RECORDER
                PRACTICE.route -> PRACTICE
                PROFILE.route -> PROFILE
                CHAT.route -> CHAT
                CAMERA.route -> CAMERA
                LOGIN.route -> LOGIN
                null -> HOME
                else -> throw IllegalArgumentException("Route $route not recognized")
            }
        }
    }

    constructor(route: String, title: String, vararg permissions: AppPermission)
            : this(route, title, permissions.toList())
}