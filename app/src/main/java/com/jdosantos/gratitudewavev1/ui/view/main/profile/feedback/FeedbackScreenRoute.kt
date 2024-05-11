package com.jdosantos.gratitudewavev1.ui.view.main.profile.feedback

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.feedbackScreenRoute(navController: NavController) {
    composable(Screen.FeedbackScreen.route) {
        FeedbackScreen(navController = navController)
    }
}