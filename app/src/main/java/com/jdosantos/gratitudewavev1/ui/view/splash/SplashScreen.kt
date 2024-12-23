package com.jdosantos.gratitudewavev1.ui.view.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.exceptions.AuthenticationException
import com.jdosantos.gratitudewavev1.ui.navigation.Screen

@Composable
fun SplashScreen(navController: NavController, splashViewModel: SplashViewModel = hiltViewModel()) {

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        splashViewModel.checkSession()
    }

    DisposableEffect(splashViewModel.isAuthenticated) {
        val observer = Observer<Result<Boolean>> { result ->

            result.onSuccess {
                navController.navigate(Screen.ContainerScreen.route) {
                    popUpTo(Screen.SplashScreen.route) { inclusive = true }
                }
            }.onFailure { exception ->
                when (exception) {
                    is AuthenticationException.EmailNotVerifiedException -> {
                        navController.navigate(Screen.VerifyEmailScreen.route)
                    }

                    is AuthenticationException.UserNotLogged -> {
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.SplashScreen.route) { inclusive = true }
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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.yuspa_logo),
            contentDescription = null, // Add appropriate content description
            modifier = Modifier.size(200.dp)
        )
    }

}
