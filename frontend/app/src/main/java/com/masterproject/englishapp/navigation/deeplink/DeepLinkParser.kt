package com.masterproject.englishapp.navigation.deeplink

import androidx.core.net.toUri

object DeepLinkParser {
    private const val SCHEME = "app"
    private const val HOST_OPEN = "open"
    private const val HOST_ACTION = "action"

    /**
     * Translate one command string to a navigation route or action.
     */
    fun parseCommand(command: String?): CommandResult {
        if (command == null) return CommandResult.None

        return try {
            val uri = command.toUri()
            if (uri.scheme != SCHEME) return CommandResult.None

            when (uri.host) {
                HOST_OPEN -> {
                    val route = uri.path?.removePrefix("/")
                    if (!route.isNullOrBlank()) CommandResult.Navigate(route) else CommandResult.None
                }
                HOST_ACTION -> {
                    val action = uri.path?.removePrefix("/") ?: ""
                    val params = uri.queryParameterNames.associateWith {
                        uri.getQueryParameter(it) ?: ""
                    }
                    CommandResult.Action(action, params)
                }
                else -> CommandResult.None
            }
        } catch (e: Exception) {
            CommandResult.None
        }
    }
}