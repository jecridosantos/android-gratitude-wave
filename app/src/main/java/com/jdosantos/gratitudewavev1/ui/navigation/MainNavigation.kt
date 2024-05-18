package com.jdosantos.gratitudewavev1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jdosantos.gratitudewavev1.ui.view.auth.login.loginScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.auth.register.registerScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.auth.reset.resetPasswordScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.auth.verify.verifyEmailScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.containerScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.goals.goalsScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.home.bycalendar.byCalendarScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.home.notesbytag.notesByTagScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.home.progress.progressScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.home.search.searchNoteScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.home.tags.searchByTagsScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.note.detailnote.detailNoteScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.note.updatenote.updateNoteScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.note.writenote.writeNoteScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.notifications.notificationsScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.profile.feedback.feedbackScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.profile.help.helpScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.SettingsViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.settingsScreenRoute
import com.jdosantos.gratitudewavev1.ui.view.splash.splashScreenRoute

@Composable
fun MainNavigation(
    settingsViewModel: SettingsViewModel
) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {


        splashScreenRoute(navController)

        loginScreenRoute(navController)

        containerScreenRoute(navController)

        writeNoteScreenRoute(navController)

        updateNoteScreenRoute(navController)

        detailNoteScreenRoute(navController)

        byCalendarScreenRoute(navController)

        registerScreenRoute(navController)

        verifyEmailScreenRoute(navController)

        notificationsScreenRoute(navController)

        searchNoteScreenRoute(navController)

        searchByTagsScreenRoute(navController)

        notesByTagScreenRoute(navController)

        resetPasswordScreenRoute(navController)

        settingsScreenRoute(navController, settingsViewModel)

        helpScreenRoute(navController)

        feedbackScreenRoute(navController)

        progressScreenRoute(navController)

        goalsScreenRoute(navController)

    }

}