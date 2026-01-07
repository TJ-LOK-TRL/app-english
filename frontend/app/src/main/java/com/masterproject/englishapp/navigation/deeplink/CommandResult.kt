package com.masterproject.englishapp.navigation.deeplink

sealed class CommandResult {
    data class Navigate(val route: String) : CommandResult()
    data class Action(val action: String, val params: Map<String, String>) : CommandResult()
    object None : CommandResult()
}