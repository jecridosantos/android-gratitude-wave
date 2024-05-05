package com.jdosantos.gratitudewavev1.ui.navigation.login

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.jdosantos.gratitudewavev1.ui.navigation.ROOT_DEEPLINK
import com.jdosantos.gratitudewavev1.ui.view.auth.login.LoginView
import com.jdosantos.gratitudewavev1.ui.view.auth.login.LoginViewModel

internal const val LOGIN_GRAPH_ROUTE = "login_graph"
private const val LOGIN_DEEPLINK = "$ROOT_DEEPLINK/login"

fun NavController.navigateToLogin() {
    navigate(LOGIN_GRAPH_ROUTE)
}

fun NavGraphBuilder.loginGraph(
    navController: NavController,
    onRegisterClick: () -> Unit,
) {
    navigation(
        startDestination = LOGIN_ROUTE,
        route = LOGIN_GRAPH_ROUTE,
        deepLinks = listOf(
            navDeepLink { uriPattern = LOGIN_DEEPLINK }
        )
    ) {
        loginScreen(
            onBackClick = {
                navController.popBackStack()
            },
            onLoggedIn = {
                navController.popBackStack()
            },
            onRegisterClick = onRegisterClick
        )
    }
}