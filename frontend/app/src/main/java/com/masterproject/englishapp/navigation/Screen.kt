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
    INTRO("introduction", "Introduction Questions"),
    HOME("home", "English Learning"),
    RECORDER("recorder", "Voice Recorder", AppPermission.RECORD_AUDIO),
    PRACTICE("practice", "Practice Exercises"),
    PROFILE("profile", "My Profile"),
    CHAT("chat", "AI Chat"),
    CAMERA("camera", "Object Camera", AppPermission.CAMERA),
    VIDEOS("videos", "Video Lessoes"),
    REGISTER("register", "Sign Up"),
    LOGIN("login", "Login");

    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route?.substringBefore("/")) {
                WELCOME.route -> WELCOME
                INTRO.route -> INTRO
                HOME.route -> HOME
                RECORDER.route -> RECORDER
                PRACTICE.route -> PRACTICE
                PROFILE.route -> PROFILE
                CHAT.route -> CHAT
                CAMERA.route -> CAMERA
                LOGIN.route -> LOGIN
                REGISTER.route -> REGISTER
                VIDEOS.route -> VIDEOS
                null -> HOME
                else -> throw IllegalArgumentException("Route $route not recognized")
            }
        }
    }

    constructor(route: String, title: String, vararg permissions: AppPermission)
            : this(route, title, permissions.toList())
}