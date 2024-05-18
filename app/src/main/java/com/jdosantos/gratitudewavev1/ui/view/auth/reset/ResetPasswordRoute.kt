package com.jdosantos.gratitudewavev1.ui.view.auth.reset

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.resetPasswordScreenRoute(navController: NavController) {
    composable(Screen.ResetPasswordScreen.route) {
        ResetPasswordScreen(navController = navController)
    }
}