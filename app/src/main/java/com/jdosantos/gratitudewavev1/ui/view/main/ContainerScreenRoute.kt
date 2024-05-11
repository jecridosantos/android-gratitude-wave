package com.jdosantos.gratitudewavev1.ui.view.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.containerScreenRoute(navController: NavController) {
    composable(Screen.ContainerScreen.route) {
        ContainerScreen(mainNavController = navController)
    }
}