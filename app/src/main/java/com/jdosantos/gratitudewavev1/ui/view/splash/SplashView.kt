package com.jdosantos.gratitudewavev1.ui.view.splash

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jdosantos.gratitudewavev1.data.local.CredentialStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/*
@Composable
fun SplashView(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    fun navigateToHome() = navController.navigate("ContainerView") {
        popUpTo("SplashView") { inclusive = true }
    }
    fun navigateToLogin() = navController.navigate("LoginView")
    fun navigateToVerifyEmail() = navController.navigate("VerifyEmailView")
    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (!currentUser?.email.isNullOrEmpty()) {
            val isEmailVerified = currentUser?.isEmailVerified

            if (isEmailVerified!!) {
                navigateToHome()
            } else {
                val dataStore = CredentialStore(context)
                dataStore.getValue().collect { value ->
                    if (value != "") {
                        val firebaseUser = FirebaseAuth.getInstance().currentUser
                        val credential =
                            EmailAuthProvider.getCredential(firebaseUser!!.email!!, value!!)

                        firebaseUser.reauthenticate(credential)
                            .addOnCompleteListener { reauthTask ->
                                if (reauthTask.isSuccessful) {
                                    if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                                        scope.launch {
                                            dataStore.saveValue("")
                                        }
                                        navigateToHome()

                                    } else {
                                        navigateToVerifyEmail()
                                    }
                                } else {
                                    navigateToVerifyEmail()
                                }
                            }
                    } else {
                        navigateToVerifyEmail()
                    }
                }
            }
        } else {
            navigateToLogin()
        }
    }


}*/

@Composable
fun SplashView(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        FirebaseAuth.getInstance().currentUser?.let { currentUser ->
            if (!currentUser.email.isNullOrEmpty()) {
                handleAuthenticatedUser(currentUser, scope, context, navController)
            } else {
                navController.navigate("LoginView")
            }
        } ?: navController.navigate("LoginView")
    }
}

private suspend fun handleAuthenticatedUser(
    currentUser: FirebaseUser,
    scope: CoroutineScope,
    context: Context,
    navController: NavController
) {
    if (currentUser.isEmailVerified) {
        navController.navigate("ContainerView") {
            popUpTo("SplashView") { inclusive = true }
        }
    } else {
        val credentialStore = CredentialStore(context)
        val storedPassword = credentialStore.getPassword().firstOrNull() ?: ""
        if (storedPassword.isNotEmpty()) {
            handleReauthentication(currentUser, storedPassword, scope, credentialStore, navController)
        } else {
            navController.navigate("VerifyEmailView")
        }
    }
}

private suspend fun handleReauthentication(
    currentUser: FirebaseUser,
    value: String,
    scope: CoroutineScope,
    credentialStore: CredentialStore,
    navController: NavController
) {
    val credential = EmailAuthProvider.getCredential(currentUser.email!!, value)
    val reauthTask = currentUser.reauthenticate(credential)
    if (reauthTask.isSuccessful && FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
        scope.launch {
            credentialStore.savePassword("")
        }
        navController.navigate("ContainerView") {
            popUpTo("SplashView") { inclusive = true }
        }
    } else {
        navController.navigate("VerifyEmailView")
    }
}
