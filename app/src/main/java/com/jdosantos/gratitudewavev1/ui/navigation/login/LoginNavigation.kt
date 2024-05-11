package com.jdosantos.gratitudewavev1.ui.navigation.login

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.view.auth.login.LoginView
import com.jdosantos.gratitudewavev1.ui.view.auth.login.LoginViewModel

internal const val LOGIN_ROUTE = "login"
internal fun NavGraphBuilder.loginScreen(
    onBackClick: () -> Unit,
    onLoggedIn: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    composable(route = LOGIN_ROUTE) {
        val viewModel: LoginViewModel = hiltViewModel()
        val isLoggedIn by viewModel.isLoggedIn.collectAsState()

        if (isLoggedIn) {
            LaunchedEffect(Unit) {
                onLoggedIn()
            }
        }

        LoginView(
            onBackClick = onBackClick,
            onLoginClick = viewModel::login,
            onRegisterClick = onRegisterClick,
            onLoginWithGoogleClick = viewModel::signInWithGoogle,
        )
    }
}