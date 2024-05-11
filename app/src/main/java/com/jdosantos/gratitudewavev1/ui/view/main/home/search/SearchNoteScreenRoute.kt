package com.jdosantos.gratitudewavev1.ui.view.main.home.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.searchNoteScreenRoute(navController: NavController) {
    composable(Screen.SearchNoteScreen.route) {
        SearchNoteScreen(navController = navController)
    }
}