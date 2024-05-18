package com.jdosantos.gratitudewavev1.ui.view.auth.reset

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.exceptions.AuthenticationException
import com.jdosantos.gratitudewavev1.ui.widget.AlertComponent
import com.jdosantos.gratitudewavev1.ui.widget.InputRound
import com.jdosantos.gratitudewavev1.ui.widget.Loader
import com.jdosantos.gratitudewavev1.utils.constants.Constants
import kotlinx.coroutines.launch

data class ResetPasswordState(
    val email: MutableState<String>,
    val success: MutableState<Boolean>,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    navController: NavController,
    resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val state = ResetPasswordState(
        email = remember { mutableStateOf("") },
        success = remember { mutableStateOf(false) },
    )
    val isLoading by resetPasswordViewModel.isLoading.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { }
    )
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.label_reset_password)) },
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
                scope.launch {
                    resetPasswordViewModel.reset(
                        state.email.value,
                    ).onSuccess { success ->
                        if (success) {
                            state.success.value = true

                        } else {
                            Toast.makeText(
                                context, R.string.label_register_error, Toast.LENGTH_LONG
                            ).show()
                        }
                    }.onFailure { exception ->

                        val errorMessage = when (exception) {
                            is AuthenticationException.EmailNotFoundException -> exception.message.toString()
                            else -> "Ha ocurrido un error. Por favor, intenta de nuevo más tarde."
                        }

                        errorMessage.let {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }


                    }
                }

            }
        )
    }
    if (isLoading) {
        Loader()
    }

    if (state.success.value) {
        AlertComponent(
            title = stringResource(R.string.message_reset_password_success_title),
            message = stringResource(R.string.message_reset_password_success_message),
            confirmText = stringResource(R.string.message_reset_password_success_confirm),
            cancelText = stringResource(R.string.message_reset_password_success_cancel),
            onConfirmClick = {
                state.success.value = false
                launcher.launch(handleGoToEmail())
            }) {
            state.success.value = false
        }
    }
}

private fun handleGoToEmail(): Intent {
    return Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_APP_EMAIL)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}

@Composable
private fun ContentRegisterView(
    paddingValues: PaddingValues,
    state: ResetPasswordState,
    onRegisterClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(Constants.SPACE_DEFAULT.dp)
    ) {


        InputRound(
            stringResource(R.string.label_email), state.email.value,
            stringResource(R.string.label_email_placeholder), KeyboardType.Email
        ) {
            state.email.value = it
        }

        Text(
            text = stringResource(id = R.string.label_message_reset_password),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(
                start = Constants.SPACE_DEFAULT.dp,
                end = Constants.SPACE_DEFAULT.dp
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        fun enabledButton() = state.email.value != ""
        Button(
            enabled = enabledButton(),
            onClick = {
                onRegisterClick()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(Constants.SPACE_DEFAULT.dp)
        ) {
            Text(text = stringResource(R.string.label_continue))
        }
    }


}