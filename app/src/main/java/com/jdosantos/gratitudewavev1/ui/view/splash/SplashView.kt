package com.jdosantos.gratitudewavev1.ui.view.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.domain.exceptions.AuthenticationException

@Composable
fun SplashView(navController: NavController, splashViewModel: SplashViewModel = hiltViewModel()) {

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        splashViewModel.checkSession()
    }

    DisposableEffect(splashViewModel.isAuthenticated) {
        val observer = Observer<Result<Boolean>> { result ->

            result.onSuccess {
                navController.navigate("ContainerView") {
                    popUpTo("SplashView") { inclusive = true }
                }
            }.onFailure { exception ->
                when (exception) {
                    is AuthenticationException.EmailNotVerifiedException -> {
                        navController.navigate("VerifyEmailView")
                    }

                    is AuthenticationException.UserNotLogged -> {
                        navController.navigate("LoginView") {
                            popUpTo("SplashView") { inclusive = true }
                        }
                    }
                }
            }
        }
        splashViewModel.isAuthenticated.observe(lifecycleOwner, observer)

        onDispose {
            splashViewModel.isAuthenticated.removeObserver(observer)
        }
    }
}
