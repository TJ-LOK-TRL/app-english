package com.masterproject.englishapp.navigation.deeplink

enum class DeepLinkAction(val routeDispatcher: String) {
    CONTEXTUAL_LESSON("contextual_lesson");

    companion object {
        fun fromRouteDispatcher(routeDispatcher: String): DeepLinkAction? {
            return DeepLinkAction.entries.find { it.routeDispatcher == routeDispatcher }
        }
    }
}