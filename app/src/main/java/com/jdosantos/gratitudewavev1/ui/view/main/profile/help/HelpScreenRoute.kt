package com.jdosantos.gratitudewavev1.ui.view.main.profile.help

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.helpScreenRoute(navController: NavController) {
    composable(Screen.HelpScreen.route) {
        HelpScreen(navController = navController)
    }
}