package com.jdosantos.gratitudewavev1.ui.view.splash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.splashScreenRoute(navController: NavController) {
    composable(Screen.SplashScreen.route) {
        SplashScreen(navController = navController)
    }
}