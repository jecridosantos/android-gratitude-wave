package com.jdosantos.gratitudewavev1.ui.view.main.note.writenote

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.writeNoteScreenRoute(navController: NavController) {
    composable(Screen.WriteNoteScreen.route) {
        WriteNoteScreen(navController = navController)
    }
}