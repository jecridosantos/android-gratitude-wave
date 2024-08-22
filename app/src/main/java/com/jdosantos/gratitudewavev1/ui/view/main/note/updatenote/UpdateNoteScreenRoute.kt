package com.jdosantos.gratitudewavev1.ui.view.main.note.updatenote

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsRouteParams

fun NavGraphBuilder.updateNoteScreenRoute(navController: NavController, updateNoteViewModel: UpdateNoteViewModel) {
    composable(Screen.UpdateNoteScreen.route, arguments = listOf(
        navArgument(ConstantsRouteParams.NOTE_DETAILS_ID) { type = NavType.StringType },
        navArgument(ConstantsRouteParams.NOTE_DETAILS_COLOR) { type = NavType.IntType }
    )) {

        val id = it.arguments?.getString(ConstantsRouteParams.NOTE_DETAILS_ID) ?: ""
        val color = it.arguments?.getInt(ConstantsRouteParams.NOTE_DETAILS_COLOR) ?: -1

        UpdateNoteScreen(id, color, navController, updateNoteViewModel)
    }
}