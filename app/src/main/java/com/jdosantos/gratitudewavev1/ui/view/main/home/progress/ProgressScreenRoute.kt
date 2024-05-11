package com.jdosantos.gratitudewavev1.ui.view.main.home.progress

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.progressScreenRoute(navController: NavController) {
    composable(Screen.ProgressScreen.route) {
        ProgressScreen(navController = navController)
    }
}