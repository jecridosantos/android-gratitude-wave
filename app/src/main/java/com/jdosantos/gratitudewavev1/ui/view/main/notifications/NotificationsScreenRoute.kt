package com.jdosantos.gratitudewavev1.ui.view.main.notifications

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.notificationsScreenRoute(navController: NavController) {
    composable(Screen.NotificationsScreen.route) {
        NotificationsScreen(navController = navController)
    }
}