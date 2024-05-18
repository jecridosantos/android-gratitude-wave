package com.jdosantos.gratitudewavev1.ui.navigation

import com.jdosantos.gratitudewavev1.utils.constants.ConstantsRouteParams.NOTE_DETAILS_COLOR
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsRouteParams.NOTE_DETAILS_ID
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsRouteParams.REMINDER_INDEX
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsRouteParams.REMINDER_INDEX_HOUR
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsRouteParams.REMINDER_INDEX_MINUTE
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsRouteParams.TAG_DETAILS_ID
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsRouteParams.TAG_DETAILS_NAME

sealed class Screen(val route: String) {

    data object SplashScreen : Screen("SplashScreen")
    data object LoginScreen : Screen("LoginScreen")
    data object ContainerScreen : Screen("ContainerScreen")
    data object WriteNoteScreen : Screen("WriteNoteScreen")
    data object UpdateNoteScreen : Screen("UpdateNoteScreen/{${NOTE_DETAILS_ID}}/{${NOTE_DETAILS_COLOR}}") {
        fun params(id: String, color: Int): String {
            return this.route.replace(oldValue = "{$NOTE_DETAILS_ID}", newValue = id)
                .replace(oldValue = "{$NOTE_DETAILS_COLOR}", newValue = "$color")
        }
    }

    data object DetailNoteScreen :
        Screen("DetailNoteScreen/{${NOTE_DETAILS_ID}}/{${NOTE_DETAILS_COLOR}}") {
        fun params(id: String, color: Int): String {
            return this.route.replace(oldValue = "{$NOTE_DETAILS_ID}", newValue = id)
                .replace(oldValue = "{$NOTE_DETAILS_COLOR}", newValue = "$color")
        }

    }

    data object ByCalendarScreen : Screen("ByCalendarScreen")
    data object RegisterScreen : Screen("RegisterScreen")
    data object ResetPasswordScreen : Screen("ResetPasswordScreen")
    data object VerifyEmailScreen : Screen("VerifyEmailScreen")
    data object NotificationsScreen : Screen("NotificationsScreen")
    data object SearchNoteScreen : Screen("SearchNoteScreen")
    data object SearchByTagsScreen : Screen("SearchByTagsScreen")

    data object NotesByTagScreen :
        Screen("NotesByTagScreen/{${TAG_DETAILS_ID}}/{${TAG_DETAILS_NAME}}") {
        fun params(id: String, name: String): String {
            return this.route.replace(oldValue = "{$TAG_DETAILS_ID}", newValue = id)
                .replace(oldValue = "{$TAG_DETAILS_NAME}", newValue = name)
        }

    }


    data object SettingsScreen : Screen("SettingsScreen")
    data object HelpScreen : Screen("HelpScreen")
    data object FeedbackScreen : Screen("FeedbackScreen")
    data object ProgressScreen : Screen("ProgressScreen")
    data object GoalsScreen : Screen("GoalsScreen")
    data object RemindersScreen : Screen("RemindersScreen")

    data object SaveRemindersScreen : Screen("SaveRemindersScreen/{$REMINDER_INDEX}/{$REMINDER_INDEX_HOUR}/{$REMINDER_INDEX_MINUTE}") {
        fun params(index: Int, hour: Int, minute: Int): String {
            return this.route.replace(oldValue = "{$REMINDER_INDEX}", newValue = "$index")
                .replace(oldValue = "{$REMINDER_INDEX_HOUR}", newValue = "$hour")
                .replace(oldValue = "{$REMINDER_INDEX_MINUTE}", newValue = "$minute")
        }
    }
}