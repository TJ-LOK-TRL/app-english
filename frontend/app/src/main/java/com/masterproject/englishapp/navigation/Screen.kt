// navigation/Screen.kt
package com.masterproject.englishapp.navigation

import com.masterproject.englishapp.navigation.deeplink.DeepLinkAction
import com.masterproject.englishapp.permissions.AppPermission
import com.masterproject.englishapp.permissions.plus

/**
 * Screens available in the English Learning Application
 *
 * @property route The display route for the screen
 * @property title The display title for the screen
 * @property showHeader Show header
 * @property showBottomBar Show bottom bar
 * @property requiredPermissions The required permissions for the screen
 */
enum class Screen(
    val route: String,
    val title: String,
    val showHeader: Boolean = true,
    val showBottomBar: Boolean = true,
    val requiredPermissions: List<AppPermission> = emptyList()
) {
    WELCOME("welcome", "Welcome", showHeader = false, showBottomBar = false),
    INTRO("introduction", "Introduction Questions", showHeader = false, showBottomBar = false),
    HOME("home", "English Learning", showHeader = false),
    RECORDER("recorder", "Voice Recorder", AppPermission.RECORD_AUDIO),
    PRACTICE("practice", "Practice Exercises", showHeader = false, showBottomBar = false),
    LESSONS("lessons", "Learn with lessons", showHeader = false, showBottomBar = false),
    CHAT("chat", "AI Chat", showHeader = true, showBottomBar = false),
    AVATAR("avatar", "Avatar 3D"),
    CAMERA("camera", "Object Camera", AppPermission.CAMERA, showHeader = false, showBottomBar = false),
    VIDEOS("videos", "Video Lessoes"),
    ACCOUNT("account", "Account", showHeader = false, showBottomBar = true),
    STATISTICS("statistics", "Statistics", showHeader = false),
    REGISTER("register", "Sign Up", showHeader = false, showBottomBar = false),
    LOGIN("login", "Login", showHeader = false, showBottomBar = false);

    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route?.substringBefore("?")?.substringBefore("/")) {
                WELCOME.route -> WELCOME
                INTRO.route -> INTRO
                HOME.route -> HOME
                RECORDER.route -> RECORDER
                PRACTICE.route -> PRACTICE
                CHAT.route -> CHAT
                AVATAR.route -> AVATAR
                CAMERA.route -> CAMERA
                LOGIN.route -> LOGIN
                ACCOUNT.route -> ACCOUNT
                REGISTER.route -> REGISTER
                VIDEOS.route -> VIDEOS
                LESSONS.route -> LESSONS
                STATISTICS.route -> STATISTICS
                DeepLinkAction.CONTEXTUAL_LESSON.routeDispatcher -> HOME // TODO: Works but needs improvement
                null -> HOME
                else -> throw IllegalArgumentException("Route $route not recognized")
            }
        }
    }

    constructor(route: String, title: String, vararg permissions: AppPermission, showHeader: Boolean = true, showBottomBar: Boolean = true)
            : this(route, title, showHeader, showBottomBar, permissions.toList())
}