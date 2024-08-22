package com.jdosantos.gratitudewavev1.ui.view.main.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jdosantos.gratitudewavev1.ui.navigation.Screen


fun NavGraphBuilder.onboardingScreenRoute(navController: NavController, viewModel: OnboardingViewModel) {
    composable(Screen.OnboardingScreen.route) {
        OnboardingScreen(navController, viewModel)
    }
}