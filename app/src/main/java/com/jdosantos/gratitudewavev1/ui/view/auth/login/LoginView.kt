package com.jdosantos.gratitudewavev1.ui.view.auth.login

import android.app.Activity
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.widget.AlertComponent
import com.jdosantos.gratitudewavev1.ui.widget.Loader

@Composable
fun LoginView(navController: NavController, loginViewModel: LoginViewModel) {

    var backPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current
    BackHandler(enabled = true) {
        backPressedOnce = if (backPressedOnce) {
            (context as? Activity)?.finish()
            false
        } else {
            true
        }
    }

    if (backPressedOnce) {
        Toast.makeText(context, stringResource(id = R.string.label_exit_to_app), Toast.LENGTH_SHORT).show()

    }


    val token = "741930850747-12p5utjs35rpjat1mipu4q4mejnks39d.apps.googleusercontent.com"

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {

                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                loginViewModel.signInWithGoogle(credential) {

                    navController.navigate("ContainerView") {
                        popUpTo("SplashView") { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                Log.d("Login google error", "error: ${e}")
            }
        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val isLoading by loginViewModel.isLoading.collectAsState()
        Text(
            text = stringResource(id = R.string.login_welcome),
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraLight
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(text = stringResource(id = R.string.login_label_input_email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)

        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(text = stringResource(id = R.string.login_label_input_pass)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {

                if (email != "" && password != "") {
                    loginViewModel.login(email, password) {isEmailVerified ->

                        if (isEmailVerified) {
                            navController.navigate("ContainerView") {
                                popUpTo("SplashView") { inclusive = true }
                            }
                        } else {
                            navController.navigate("VerifyEmailView")
                        }
                    }
                }

            }, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
        ) {
            Text(text = stringResource(id = R.string.login_button_login))
        }


        Button(
            onClick = {
                val options = GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN
                ).requestIdToken(token)
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, options)

                launcher.launch(googleSignInClient.signInIntent)

            }, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.googleicon), contentDescription = "", Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = stringResource(id = R.string.label_signin_with_google))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.login_button_forgot_password),
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            color =  MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { })
        Spacer(modifier = Modifier.height(16.dp))

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
                    color =  MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        navController.navigate("RegisterView")
                    })
            }
        }

        if (isLoading) {
            Loader()
        }

        if (loginViewModel.showAlert) {
            AlertComponent(title = stringResource(id = R.string.login_alert_title),
                message = stringResource(id = R.string.login_alert_message),
                confirmText = stringResource(id = R.string.label_confirm),
                cancelText = null,
                onConfirmClick = { loginViewModel.closeAlert() }) {

            }
        }

    }

}