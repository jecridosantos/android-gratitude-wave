package com.jdosantos.gratitudewavev1.ui.view.auth.register

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.registerScreenRoute(navController: NavController) {
    composable(Screen.RegisterScreen.route) {
        RegisterScreen(navController = navController)
    }
}