package com.jdosantos.gratitudewavev1.ui.view.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.jdosantos.gratitudewavev1.app.store.CredentialStore
import kotlinx.coroutines.launch

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


}