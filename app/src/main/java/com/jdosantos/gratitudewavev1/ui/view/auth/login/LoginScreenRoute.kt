package com.jdosantos.gratitudewavev1.ui.view.auth.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.loginScreenRoute(navController: NavController) {
    composable(Screen.LoginScreen.route) {
        LoginScreen(navController = navController)
    }
}