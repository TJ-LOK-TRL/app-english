// navigation/Screen.kt
package com.masterproject.englishapp.navigation

/**
 * Screens available in the English Learning Application
 *
 * @property route The display route for the screen
 * @property title The display title for the screen
 */
enum class Screen(val route: String, val title: String) {
    HOME("home", "English Learning"),
    RECORDER("recorder", "Voice Recorder"),
    PRACTICE("practice", "Practice Exercises"),
    PROFILE("profile", "My Profile"),
    CHAT("chat", "AI Chat");

    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route?.substringBefore("/")) {
                HOME.route -> HOME
                RECORDER.route -> RECORDER
                PRACTICE.route -> PRACTICE
                PROFILE.route -> PROFILE
                CHAT.route -> CHAT
                null -> HOME
                else -> throw IllegalArgumentException("Route $route not recognized")
            }
        }
    }
}