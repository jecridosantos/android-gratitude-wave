package com.jdosantos.gratitudewavev1.ui.view.auth.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.app.store.CredentialStore
import com.jdosantos.gratitudewavev1.ui.widget.AlertComponent
import com.jdosantos.gratitudewavev1.ui.widget.InputRound
import com.jdosantos.gratitudewavev1.ui.widget.Loader
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(navController: NavController, registerViewModel: RegisterViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Registro") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                }
            )
        }
    ) { paddingValues ->
        ContentRegisterView(paddingValues, navController, registerViewModel)
    }
}

@Composable
fun ContentRegisterView(
    paddingValues: PaddingValues,
    navController: NavController,
    registerViewModel: RegisterViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = CredentialStore(context)
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by registerViewModel.isLoading.collectAsState()
    Column(

        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
    ) {


        InputRound("Name", name, "Name", KeyboardType.Text) {
            name = it
        }

        InputRound("Email", email, "Email", KeyboardType.Email) {
            email = it
        }

        InputRound("Password", password, "Password", KeyboardType.Password) {
            password = it
        }

        Text(
            text = "By creating account, you agree to the Private Policy and Terms and Conditions.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "We need verify your email to you can use our app.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        fun enabledButton() = email != "" && name != "" && password != ""
        Button(
            enabled = enabledButton(),
            onClick = {
                registerViewModel.register(email, name, password) {
                    scope.launch {
                        dataStore.saveValue(password)
                    }
                    navController.navigate("VerifyEmailView")
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Continuar")
        }
    }


    if (isLoading) {
        Loader()
    }

    if (registerViewModel.showAlert) {
        AlertComponent(title = "Error en registro",
            message = "Error en registro",
            confirmText = stringResource(id = R.string.label_confirm),
            cancelText = null,
            onConfirmClick = { registerViewModel.closeAlert() }) {

        }
    }


}