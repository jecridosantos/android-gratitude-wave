package com.jdosantos.gratitudewavev1.ui.view.auth.login

import android.annotation.SuppressLint
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.data.local.CredentialStore
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.ui.widget.InputRound
import com.jdosantos.gratitudewavev1.ui.widget.Loader
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.GOOGLE_TOKEN
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MAX
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID
import kotlinx.coroutines.launch

data class LoginViewState(
    val backPressedOnce: MutableState<Boolean>,
    val email: MutableState<String>,
    val password: MutableState<String>,
)

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val dataStore = CredentialStore(context)
    val scope = rememberCoroutineScope()

    val state = LoginViewState(
        backPressedOnce = remember { mutableStateOf(false) },
        email = remember { mutableStateOf("") },
        password = remember { mutableStateOf("") },
    )

    loginViewModel.toastMessage.observe(LocalLifecycleOwner.current, Observer { message ->
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    })

    val isLoading by loginViewModel.isLoading.collectAsState()

    val loginSuccess by loginViewModel.loginSuccess.collectAsState()

    val unverifiedEmail by loginViewModel.unverifiedEmail.collectAsState()

    BackHandler(enabled = true) {
        handleBackPressed(context, state.backPressedOnce)
    }
    val launcherIntentGoogle = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            loginViewModel.signInWithGoogle(credential)
        } catch (e: Exception) {
            Log.e("Login google error", "error: $e")
        }
    }
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            navController.navigate(Screen.ContainerScreen.route) {
                popUpTo(Screen.LoginScreen.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(unverifiedEmail) {
        if (unverifiedEmail) {

            val password = state.password.value
            scope.launch {
                dataStore.savePassword(password)
            }

            navController.navigate(Screen.VerifyEmailScreen.route) {
                popUpTo(Screen.LoginScreen.route) { inclusive = true }
            }
        }
    }

    Loader(show = isLoading)

    LoginScreenContent(
        state = state,
        onEmailChanged = { state.email.value = it },
        onPasswordChanged = { state.password.value = it },
        onLoginClicked = {
            handleLoginClick(
                loginViewModel,
                state.email.value,
                state.password.value
            )
        },
        onGoogleSignInClicked = { launcherIntentGoogle.launch(googleSignInIntent(context)) },
        onForgotPasswordClicked = { navController.navigate(Screen.ResetPasswordScreen.route) },
        onSignUpClicked = { navController.navigate(Screen.RegisterScreen.route) }
    )
}

@Composable
fun LoginScreenContent(
    state: LoginViewState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onGoogleSignInClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onSignUpClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(SPACE_DEFAULT.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Image(
            painter = painterResource(id = R.drawable.elefante_gratitud),
            contentDescription = null,
            Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(SPACE_DEFAULT_MAX.dp))
        Text(
            text = stringResource(id = R.string.login_welcome),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(SPACE_DEFAULT_MAX.dp))

        InputRound(
            label = stringResource(R.string.login_label_input_email),
            value = state.email.value,
            placeholder = stringResource(R.string.login_label_input_email),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ) {
            onEmailChanged(it)
        }
        InputRound(
            label = stringResource(R.string.label_password),
            value = state.password.value,
            placeholder = stringResource(R.string.label_password_placeholder),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ) {
            onPasswordChanged(it)
        }

        ElevatedButton(
            onClick = {
                onLoginClicked()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = stringResource(id = R.string.login_button_login))
        }
        Spacer(modifier = Modifier.height(SPACE_DEFAULT_MID.dp))
        ElevatedButton(
            onClick = {
                onGoogleSignInClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp),
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
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                onForgotPasswordClicked()
            })
        Spacer(modifier = Modifier.height(SPACE_DEFAULT.dp))

        Row {

            Text(
                text = stringResource(id = R.string.login_text_no_account),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = stringResource(id = R.string.login_button_to_sign_up),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    onSignUpClicked()
                }
            )

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