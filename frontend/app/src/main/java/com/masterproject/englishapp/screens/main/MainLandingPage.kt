// screens/MainLandingPage.kt
package com.masterproject.englishapp.screens.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.masterproject.englishapp.components.BottomNavigationBar
import com.masterproject.englishapp.components.headers.CommonHeader
import com.masterproject.englishapp.components.loaders.AppSplashScreen
import com.masterproject.englishapp.event.UiEvent
import com.masterproject.englishapp.location.LocationContextHandler
import com.masterproject.englishapp.navigation.MainNavigation
import com.masterproject.englishapp.navigation.Navigator
import com.masterproject.englishapp.navigation.Screen
import com.masterproject.englishapp.notification.dailyreminder.DailyReminderHandler
import com.masterproject.englishapp.permissions.PermissionManager

@Composable
fun MainLandingPage(
    permissionManager: PermissionManager,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    if (mainViewModel.isInitializing) {
        AppSplashScreen()
    } else {
        val uiEventService = mainViewModel.uiEventService
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        val navigator = Navigator(navController, permissionManager)
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val startRoute = if (mainViewModel.isLoggedIn) Screen.HOME.route else Screen.WELCOME.route
        val currentScreen = remember(currentRoute) { Screen.fromRoute(currentRoute) }

        LaunchedEffect(Unit) {
            uiEventService.events.collect { event ->
                when (event) {
                    is UiEvent.Error ->
                        snackbarHostState.showSnackbar(
                            message = event.message
                        )

                    is UiEvent.Info ->
                        snackbarHostState.showSnackbar(
                            message = event.message
                        )

                    is UiEvent.Success ->
                        snackbarHostState.showSnackbar(
                            message = event.message
                        )

                    is UiEvent.Warning ->
                        snackbarHostState.showSnackbar(
                            message = event.message
                        )
                }
            }
        }

        LaunchedEffect(mainViewModel.lunchedRoute) {
            val pendingRoute = mainViewModel.lunchedRoute
            if (pendingRoute != null && mainViewModel.isLoggedIn) {
                Log.d("MainLandingPage", "Lunch screen: ${pendingRoute}.")

                // TODO: Try normalize navigator usage with dispatchers and screens
                navController.navigate(pendingRoute) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                }

                mainViewModel.clearLunchedRoute()
            }
        }

        LocationContextHandler(
            permissionManager = permissionManager,
            userPreferencesStore = mainViewModel.userPreferencesStore
        )

        DailyReminderHandler(
            userPreferencesStore = mainViewModel.userPreferencesStore
        )

        Scaffold(
            topBar = {
                if (currentScreen.showHeader) {
                    CommonHeader(
                        navController = navController,
                        onBackClick = { navController.navigateUp() }
                    )
                }
            },

            bottomBar = {
                if (currentScreen.showBottomBar) {
                    BottomNavigationBar(navigator, currentRoute)
                }
            },

            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                MainNavigation(navigator, startDestination = startRoute)
            }
        }
    }
}