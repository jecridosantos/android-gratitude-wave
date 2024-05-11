package com.jdosantos.gratitudewavev1.ui.view.main.home.tags

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

fun NavGraphBuilder.searchByTagsScreenRoute(navController: NavController) {
    composable(Screen.SearchByTagsScreen.route) {
        SearchByTagsScreen(navController = navController)
    }
}