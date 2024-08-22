package com.jdosantos.gratitudewavev1.ui.view.main.webview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsRouteParams

fun NavGraphBuilder.webViewScreenRoute(navController: NavController) {
    composable(Screen.WebViewScreen.route, arguments = listOf(
        navArgument(ConstantsRouteParams.WEB_VIEW_TITLE) { type = NavType.IntType },
        navArgument(ConstantsRouteParams.WEB_VIEW_PATH) { type = NavType.StringType }
    )) {

        val title = it.arguments?.getInt(ConstantsRouteParams.WEB_VIEW_TITLE) ?: -1
        val path = it.arguments?.getString(ConstantsRouteParams.WEB_VIEW_PATH) ?: ""

        WebViewScreen(title, path, navController)
    }
}