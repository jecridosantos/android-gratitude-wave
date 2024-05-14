package com.jdosantos.gratitudewavev1.ui.view.main.profile.settings

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.reminders.RemindersScreen
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.reminders.SaveRemindersScreen
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.REMINDER_INDEX_EMPTY
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsRouteParams

fun NavGraphBuilder.settingsScreenRoute(
    navController: NavController,
    settingsViewModel: SettingsViewModel
) {
    composable(Screen.SettingsScreen.route) {
        SettingsScreen(navController = navController, settingsViewModel)
    }

    composable(Screen.RemindersScreen.route) {
        RemindersScreen(navController = navController, settingsViewModel)
    }

    composable(
        Screen.SaveRemindersScreen.route, arguments = listOf(
            navArgument(ConstantsRouteParams.REMINDER_INDEX) { type = NavType.IntType },
            navArgument(ConstantsRouteParams.REMINDER_INDEX_HOUR) { type = NavType.IntType },
            navArgument(ConstantsRouteParams.REMINDER_INDEX_MINUTE) { type = NavType.IntType },
        )
    ) {
        val id = it.arguments?.getInt(ConstantsRouteParams.REMINDER_INDEX) ?: REMINDER_INDEX_EMPTY
        val hour =
            it.arguments?.getInt(ConstantsRouteParams.REMINDER_INDEX_HOUR) ?: REMINDER_INDEX_EMPTY
        val minute =
            it.arguments?.getInt(ConstantsRouteParams.REMINDER_INDEX_MINUTE) ?: REMINDER_INDEX_EMPTY
        Log.d("TIMEPICKER 3", "index: $id, hour: $hour, minute: $minute")

        SaveRemindersScreen(id, hour, minute, navController = navController, settingsViewModel)
    }
}
