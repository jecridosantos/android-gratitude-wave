package com.jdosantos.gratitudewavev1.ui.view.main.profile.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.reminders.RemindersScreen
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.reminders.SaveRemindersScreen

fun NavGraphBuilder.settingsScreenRoute(navController: NavController) {
    composable(Screen.SettingsScreen.route) {
        SettingsScreen(navController = navController)
    }
}

fun NavGraphBuilder.remindersScreenRoute(navController: NavController) {
    composable(Screen.RemindersScreen.route) {
        RemindersScreen(navController = navController)
    }
}

fun NavGraphBuilder.saveRemindersScreenRoute(navController: NavController) {
    composable(Screen.SaveRemindersScreen.route) {
        SaveRemindersScreen(navController = navController)
    }
}