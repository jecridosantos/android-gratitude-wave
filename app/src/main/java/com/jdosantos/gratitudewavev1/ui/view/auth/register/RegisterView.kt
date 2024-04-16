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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.jdosantos.gratitudewavev1.core.common.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.ui.widget.AlertComponent
import com.jdosantos.gratitudewavev1.ui.widget.InputRound
import com.jdosantos.gratitudewavev1.ui.widget.Loader
import kotlinx.coroutines.launch

data class RegisterViewState(
    val name: MutableState<String>,
    val email: MutableState<String>,
    val password: MutableState<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterView(navController: NavController, registerViewModel: RegisterViewModel) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = CredentialStore(context)

    val state = RegisterViewState(
        name = remember { mutableStateOf("") },
        email = remember { mutableStateOf("") },
        password = remember { mutableStateOf("") }
    )
    val isLoading by registerViewModel.isLoading.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.label_register)) },
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
        ContentRegisterView(
            paddingValues,
            state,
            onRegisterClick = {
                registerViewModel.register(
                    state.email.value,
                    state.name.value,
                    state.password.value
                ) {
                    scope.launch {
                        dataStore.savePassword(state.password.value)
                    }
                    navController.navigate("VerifyEmailView")
                }
            }
        )
    }
    if (isLoading) {
        Loader()
    }
    if (registerViewModel.showAlert) {
        AlertComponent(title = stringResource(R.string.label_error_register),
            message = stringResource(R.string.label_error_register),
            confirmText = stringResource(id = R.string.label_confirm),
            cancelText = null,
            onConfirmClick = { registerViewModel.closeAlert() }) {
        }
    }
}

@Composable
private fun ContentRegisterView(
    paddingValues: PaddingValues,
    state: RegisterViewState,
    onRegisterClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(SPACE_DEFAULT.dp)
    ) {

        InputRound(
            stringResource(R.string.label_name), state.name.value,
            stringResource(R.string.label_name_placeholder), KeyboardType.Text
        ) {
            state.name.value = it
        }

        InputRound(
            stringResource(R.string.label_email), state.email.value,
            stringResource(R.string.label_email_placeholder), KeyboardType.Email
        ) {
            state.email.value = it
        }

        InputRound(
            stringResource(R.string.label_password), state.password.value,
            stringResource(R.string.label_password_placeholder), KeyboardType.Password
        ) {
            state.password.value = it
        }

        Text(
            text = stringResource(id = R.string.label_message_accept_conditions),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
        )

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(id = R.string.label_message_verify_email),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
        )
        fun enabledButton() =
            state.name.value != "" && state.email.value != "" && state.password.value != ""
        Button(
            enabled = enabledButton(),
            onClick = {
                onRegisterClick()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(SPACE_DEFAULT.dp)
        ) {
            Text(text = stringResource(R.string.label_continue))
        }
    }


}