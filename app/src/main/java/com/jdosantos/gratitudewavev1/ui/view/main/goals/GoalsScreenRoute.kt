package com.jdosantos.gratitudewavev1.ui.view.main.goals

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.goalsScreenRoute(navController: NavController) {
    composable(Screen.GoalsScreen.route) {
        GoalsScreen(navController = navController)
    }
}