package com.masterproject.englishapp.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.masterproject.englishapp.event.UiEvent
import com.masterproject.englishapp.permissions.PermissionManager
import kotlinx.coroutines.flow.MutableSharedFlow

interface NavigationActions {
    fun navigate(
        screen: Screen,
        params: String? = null,
        navOptions: (NavOptionsBuilder.() -> Unit) = { }
    )

    fun navigateUp(
        fallbackScreen: Screen? = null,
        navOptions: (NavOptionsBuilder.() -> Unit) = { }
    )
}

class Navigator(
    private val navController: NavHostController,
    private val permissionManager: PermissionManager,
) : NavigationActions {
    val controller: NavHostController
        get() = navController

    override fun navigate(
        screen: Screen,
        params: String?,
        navOptions: (NavOptionsBuilder.() -> Unit)
    ) {
        permissionManager.ensurePermissions(
            required = screen.requiredPermissions,
            onGranted = {
                val route = if (params != null) screen.route + params else screen.route
                navController.navigate(route, navOptions)
            },
            onDenied = {
                TODO()
            }
        )
    }

    override fun navigateUp(
        fallbackScreen: Screen?,
        navOptions: (NavOptionsBuilder.() -> Unit)
    ) {
        val previousRoute = navController.previousBackStackEntry?.destination?.route
        val previousScreen = previousRoute?.let { route ->
            Screen.fromRoute(route)
        } ?: fallbackScreen

        if (previousScreen != null) {
            navigate(previousScreen, navOptions = navOptions)
        }
    }
}