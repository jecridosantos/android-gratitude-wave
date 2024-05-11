package com.jdosantos.gratitudewavev1.ui.view.auth.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.data.local.CredentialStore
import com.jdosantos.gratitudewavev1.domain.exceptions.AuthenticationException
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.ui.widget.InputRound
import com.jdosantos.gratitudewavev1.ui.widget.Loader
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.GOOGLE_TOKEN
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MAX
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

data class LoginViewState(
    val backPressedOnce: MutableState<Boolean>,
    val email: MutableState<String>,
    val password: MutableState<String>
)

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val dataStore = CredentialStore(context)
    val scope = rememberCoroutineScope()

    val state = LoginViewState(
        backPressedOnce = remember { mutableStateOf(false) },
        email = remember { mutableStateOf("") },
        password = remember { mutableStateOf("") }
    )

    BackHandler(enabled = true) {
        handleBackPressed(context, state.backPressedOnce)
    }
    val isLoading by loginViewModel.isLoading.collectAsState()
    val launcherIntentGoogle = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            loginViewModel.signInWithGoogle(credential) { success ->
                if (success) handleGoogleSignInResult(navController)
            }
        } catch (e: Exception) {
            Log.e("Login google error", "error: $e")
        }
    }

    DisposableEffect(loginViewModel.loginResult) {
        val observer = Observer<Result<User>> { result ->

            result.onSuccess {
                navController.navigate(Screen.ContainerScreen.route) {
                    popUpTo("LoginView") { inclusive = true }
                }
            }.onFailure {exception ->
                val errorMessage = when (exception) {
                    is AuthenticationException.EmailNotVerifiedException -> {
                        scope.launch {
                            scope.async {
                                val password = state.password.value
                                dataStore.savePassword(password)

                            }.await()

                            navController.navigate(Screen.VerifyEmailScreen.route){
                                popUpTo(Screen.LoginScreen.route) { inclusive = true }
                            }
                        }
                        null
                    }
                    is AuthenticationException.InvalidCredentialsException -> exception.message
                    is AuthenticationException.GenericAuthenticationException -> exception.message
                    else ->
                        "Ha ocurrido un error. Por favor, intenta de nuevo más tarde."
                }

                errorMessage?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }


        }
        loginViewModel.loginResult.observe(lifecycleOwner, observer)

        onDispose {
            loginViewModel.loginResult.removeObserver(observer)
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(SPACE_DEFAULT.dp)
    ) {
        Text(
            text = stringResource(id = R.string.login_welcome),
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraLight
        )

        Spacer(modifier = Modifier.height(SPACE_DEFAULT_MAX.dp))

        InputRound(
            stringResource(R.string.login_label_input_email), state.email.value,
            stringResource(R.string.login_label_input_email), KeyboardType.Email
        ) {
            state.email.value = it
        }
        InputRound(
            stringResource(R.string.label_password), state.password.value,
            stringResource(R.string.label_password_placeholder), KeyboardType.Password
        ) {
            state.password.value = it
        }

        Button(
            onClick = {
                handleLoginClick(loginViewModel, state.email.value, state.password.value)
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
        ) {
            Text(text = stringResource(id = R.string.login_button_login))
        }

        Button(
            onClick = {
                launcherIntentGoogle.launch(googleSignInIntent(context))
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.googleicon),
                contentDescription = "",
                Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(SPACE_DEFAULT.dp))
            Text(text = stringResource(id = R.string.label_signin_with_google))
        }

        Spacer(modifier = Modifier.height(SPACE_DEFAULT.dp))
        Text(text = stringResource(id = R.string.login_button_forgot_password),
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { })
        Spacer(modifier = Modifier.height(SPACE_DEFAULT.dp))

        Row {
            Column {
                Text(text = stringResource(id = R.string.login_text_no_account),
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { })
            }
            Spacer(modifier = Modifier.width(5.dp))
            Column {
                Text(text = stringResource(id = R.string.login_button_to_sign_up),
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.RegisterScreen.route)
                    })
            }
        }

        if (isLoading) {
            Loader()
        }
    }

}

private fun handleBackPressed(
    context: Context,
    backPressedOnceState: MutableState<Boolean>
) {
    if (backPressedOnceState.value) {
        Toast.makeText(context, R.string.label_exit_to_app, Toast.LENGTH_SHORT).show()
        (context as? Activity)?.finish()
    } else {
        backPressedOnceState.value = true
    }
}



private fun handleGoogleSignInResult(navController: NavController) {
    navController.navigate(Screen.ContainerScreen.route) {
        popUpTo(Screen.SplashScreen.route) { inclusive = true }
    }
}

private fun googleSignInIntent(context: Context): Intent {
    val options = GoogleSignInOptions.Builder(
        GoogleSignInOptions.DEFAULT_SIGN_IN
    ).requestIdToken(GOOGLE_TOKEN)
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, options)
    return googleSignInClient.signInIntent
}

private fun handleLoginClick(
    loginViewModel: LoginViewModel,
    email: String,
    password: String
) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        loginViewModel.signInWithEmailAndPassword(email, password)
    }
}