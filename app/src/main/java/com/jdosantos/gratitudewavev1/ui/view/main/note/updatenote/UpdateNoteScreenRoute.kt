package com.jdosantos.gratitudewavev1.ui.view.main.note.updatenote

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.updateNoteScreenRoute(navController: NavController) {
    composable(Screen.UpdateNoteScreen.route) {
        UpdateNoteScreen(navController = navController)
    }
}