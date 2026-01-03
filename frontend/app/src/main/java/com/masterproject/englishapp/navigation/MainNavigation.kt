// navigation/MainNavigation.kt
package com.masterproject.englishapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.masterproject.englishapp.navigation.params.ExerciseParams
import com.masterproject.englishapp.screens.HomeScreen
import com.masterproject.englishapp.screens.camera.CameraScreen
import com.masterproject.englishapp.screens.exercises.ExerciseFlowManager
import com.masterproject.englishapp.screens.auth.login.LoginScreen
import com.masterproject.englishapp.screens.auth.register.RegisterFlowManager
import com.masterproject.englishapp.screens.intro.questions.QuestionFlowManager
import com.masterproject.englishapp.screens.intro.welcome.WelcomeScreen
import com.masterproject.englishapp.screens.lessons.videos.VideoLessonsScreen

@Composable
fun MainNavigation(
    navigator: Navigator,
    startDestination: String
) {
    NavHost(
        navController = navigator.controller,
        startDestination = startDestination
    ) {
        composable(Screen.WELCOME.route) {
            WelcomeScreen(navigator)
        }

        composable(Screen.INTRO.route) {
            QuestionFlowManager(navigator)
        }

        composable(Screen.REGISTER.route) {
            RegisterFlowManager(navigator)
        }

        composable(Screen.LOGIN.route) {
            LoginScreen(onLoginSuccess = {
                navigator.navigate(Screen.HOME)
            })
        }

        composable(Screen.HOME.route) {
            HomeScreen(navigator)
        }

        composable(
            route = "${Screen.PRACTICE.route}?types={types}&categories={categories}&language={language}",
            arguments = listOf(
                navArgument("types") { defaultValue = "" },
                navArgument("categories") { defaultValue = "" },
                navArgument("language") { defaultValue = "en" }
            )
        ) { backStackEntry ->
            val params = ExerciseParams.parseQuery(
                types = backStackEntry.arguments?.getString("types"),
                categories = backStackEntry.arguments?.getString("categories"),
                language = backStackEntry.arguments?.getString("language")
            )

            ExerciseFlowManager(
                navigator = navigator,
                exerciseTypes = params.exerciseTypes,
                categories = params.categories
            )
        }

        composable(Screen.PROFILE.route) {
            //ProfileScreen(
            //    onNavigateBack = { navController.navigateUp() }
            //)
        }

        //composable(Screen.CHAT.route) {
        //    ChatScreen(
        //        recorder = recorder
        //    )
        //}

        composable(Screen.CAMERA.route) {
            CameraScreen()
        }

        composable(Screen.VIDEOS.route) {
            VideoLessonsScreen()
        }
    }
}
