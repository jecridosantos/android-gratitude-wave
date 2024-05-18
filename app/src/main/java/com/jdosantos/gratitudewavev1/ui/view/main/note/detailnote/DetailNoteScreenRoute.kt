package com.jdosantos.gratitudewavev1.ui.view.main.note.detailnote

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.ui.view.main.note.sharednote.SharedNoteScreen

fun NavGraphBuilder.detailNoteScreenRoute(navController: NavController) {
    composable(Screen.DetailNoteScreen.route) {
        DetailNoteScreen(navController = navController)
    }

    composable(Screen.SharedScreen.route) {
        SharedNoteScreen(navController)
    }
}