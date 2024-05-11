package com.jdosantos.gratitudewavev1.ui.view.main.home.notesbytag

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.notesByTagScreenRoute(navController: NavController) {
    composable(Screen.NotesByTagScreen.route) {
        NotesByTagScreen(navController = navController)
    }
}