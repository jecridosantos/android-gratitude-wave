package com.jdosantos.gratitudewavev1.ui.view.main.home.bycalendar

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.byCalendarScreenRoute(navController: NavController) {
    composable(Screen.ByCalendarScreen.route) {
        ByCalendarScreen(navController = navController)
    }
}