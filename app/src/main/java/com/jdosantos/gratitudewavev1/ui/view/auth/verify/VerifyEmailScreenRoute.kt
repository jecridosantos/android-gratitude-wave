package com.jdosantos.gratitudewavev1.ui.view.auth.verify

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.verifyEmailScreenRoute(navController: NavController) {
    composable(Screen.VerifyEmailScreen.route) {
        VerifyEmailScreen(navController = navController)
    }
}