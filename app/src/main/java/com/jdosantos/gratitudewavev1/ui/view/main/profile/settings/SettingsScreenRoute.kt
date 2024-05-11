package com.jdosantos.gratitudewavev1.ui.view.main.profile.settings

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.reminders.RemindersScreen
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.reminders.SaveRemindersScreen

fun NavGraphBuilder.settingsScreenRoute(navController: NavController, settingsViewModel: SettingsViewModel) {
    composable(Screen.SettingsScreen.route) {
        SettingsScreen(navController = navController, settingsViewModel)
    }

    composable(Screen.RemindersScreen.route) {
        RemindersScreen(navController = navController,settingsViewModel)
    }

    composable(Screen.SaveRemindersScreen.route) {
        SaveRemindersScreen(navController = navController, settingsViewModel)
    }
}
