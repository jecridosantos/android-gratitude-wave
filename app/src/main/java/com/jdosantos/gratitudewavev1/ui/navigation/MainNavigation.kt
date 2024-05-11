package com.jdosantos.gratitudewavev1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jdosantos.gratitudewavev1.ui.navigation.login.loginGraph
import com.jdosantos.gratitudewavev1.ui.view.auth.login.LoginView
import com.jdosantos.gratitudewavev1.ui.view.auth.login.LoginViewModel
import com.jdosantos.gratitudewavev1.ui.view.auth.register.RegisterView
import com.jdosantos.gratitudewavev1.ui.view.auth.register.RegisterViewModel
import com.jdosantos.gratitudewavev1.ui.view.auth.verify.VerifyEmailView
import com.jdosantos.gratitudewavev1.ui.view.auth.verify.VerifyEmailViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.ContainerView
import com.jdosantos.gratitudewavev1.ui.view.main.goals.GoalsView
import com.jdosantos.gratitudewavev1.ui.view.main.goals.GoalsViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.home.HomeViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.home.bycalendar.ByCalendarView
import com.jdosantos.gratitudewavev1.ui.view.main.home.bycalendar.ByCalendarViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.home.notesbytag.NotesByTagView
import com.jdosantos.gratitudewavev1.ui.view.main.home.notesbytag.NotesByTagViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.home.progress.ProgressView
import com.jdosantos.gratitudewavev1.ui.view.main.home.progress.ProgressViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.home.search.SearchNoteView
import com.jdosantos.gratitudewavev1.ui.view.main.home.search.SearchNoteViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.home.tags.SearchByTagsView
import com.jdosantos.gratitudewavev1.ui.view.main.home.tags.SearchByTagsViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.note.detailnote.DetailNoteView
import com.jdosantos.gratitudewavev1.ui.view.main.note.detailnote.DetailNoteViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.note.updatenote.UpdateNoteView
import com.jdosantos.gratitudewavev1.ui.view.main.note.updatenote.UpdateNoteViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.note.writenote.WriteNoteView
import com.jdosantos.gratitudewavev1.ui.view.main.note.writenote.WriteNoteViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.notifications.NotificationsView
import com.jdosantos.gratitudewavev1.ui.view.main.notifications.NotificationsViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.profile.ProfileViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.profile.feedback.FeedbackView
import com.jdosantos.gratitudewavev1.ui.view.main.profile.feedback.FeedbackViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.profile.help.HelpView
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.SettingsView
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.SettingsViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.reminders.RemindersView
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.reminders.SaveRemindersView
import com.jdosantos.gratitudewavev1.ui.view.splash.SplashView

@Composable
fun MainNavigation(
    loginViewModel: LoginViewModel,
    writeNoteViewModel: WriteNoteViewModel,
    profileViewModel: ProfileViewModel,
    homeViewModel: HomeViewModel,
    detailNoteViewModel: DetailNoteViewModel,
    updateNoteViewModel: UpdateNoteViewModel,
    byCalendarViewModel: ByCalendarViewModel,
    registerViewModel: RegisterViewModel,
    verifyEmailViewModel: VerifyEmailViewModel,
    searchNoteViewModel: SearchNoteViewModel,
    searchByTagsViewModel: SearchByTagsViewModel,
    notesByTagViewModel: NotesByTagViewModel,
    progressViewModel: ProgressViewModel,
    goalsViewModel: GoalsViewModel,
    notificationsViewModel: NotificationsViewModel,
    settingsViewModel: SettingsViewModel,
    eedbackViewModel: FeedbackViewModel
) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "SplashView") {
        composable("SplashView") {
            SplashView(navController = navController)
        }


/*
        composable("LoginView") {
            LoginView(navController, loginViewModel)
        }
*/
        loginGraph(
            navController = navController,
            onRegisterClick = {
                navController.navigate("RegisterView");
            }
        )

        composable("ContainerView") {
            ContainerView(
                navController,
                profileViewModel,
                homeViewModel
            )
        }

        composable("WriteNoteView") {

            WriteNoteView(writeNoteViewModel, navController)
        }

        composable("UpdateNoteView/{id}/{color}", arguments = listOf(
            navArgument("id") { type = NavType.StringType },
            navArgument("color") { type = NavType.IntType }
        )) {

            val id = it.arguments?.getString("id") ?: ""
            val color = it.arguments?.getInt("color") ?: -1
            UpdateNoteView(id, color, updateNoteViewModel, navController)
        }

        composable("DetailNoteView/{id}/{color}", arguments = listOf(
            navArgument("id") { type = NavType.StringType },
            navArgument("color") { type = NavType.IntType }
        )) {

            val id = it.arguments?.getString("id") ?: ""
            val color = it.arguments?.getInt("color") ?: -1
            DetailNoteView(id, color, detailNoteViewModel, navController)
        }

        composable("ByCalendarView") {
            ByCalendarView(byCalendarViewModel, navController)
        }

        composable("RegisterView") {
            RegisterView(navController, registerViewModel)
        }

        composable("VerifyEmailView") {
            VerifyEmailView(navController, verifyEmailViewModel)
        }

        composable("NotificationsView") {
            NotificationsView(navController, notificationsViewModel)
        }

        composable("SearchNoteView") {
            SearchNoteView(searchNoteViewModel, navController)
        }

        composable("SearchByTagsView") {
            SearchByTagsView(searchByTagsViewModel, navController)
        }

        composable("NotesByTagView/{tagId}/{tagName}", arguments = listOf(
            navArgument("tagId") { type = NavType.StringType },
            navArgument("tagName") { type = NavType.StringType }
        )) {

            val tagId = it.arguments?.getString("tagId") ?: ""
            val tagName = it.arguments?.getString("tagName") ?: ""
            NotesByTagView(tagId, tagName, notesByTagViewModel, navController)
        }

        composable("SettingsView") {
            SettingsView(navController, settingsViewModel)
        }

        composable("HelpView") {
            HelpView(navController)
        }

        composable("FeedbackView") {
            FeedbackView(navController, eedbackViewModel)
        }

        composable("ProgressView") {
            ProgressView(progressViewModel, navController)
        }

        composable("GoalsView") {
            GoalsView(navController, goalsViewModel)
        }
        composable("RemindersView") {
            RemindersView(navController, settingsViewModel)
        }
        composable("SaveRemindersView/{id}", arguments = listOf(
            navArgument("id") { type = NavType.IntType }
        )) {
            val id = it.arguments?.getInt("id") ?: -1
            SaveRemindersView(id, navController, settingsViewModel)
        }
    }

}