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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.widget.AlertComponent
import com.jdosantos.gratitudewavev1.ui.widget.InputRound
import com.jdosantos.gratitudewavev1.ui.widget.Loader
import com.jdosantos.gratitudewavev1.utils.constants.Constants


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    navController: NavController,
    resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val email by resetPasswordViewModel.email.collectAsState()
    val isButtonEnabled by resetPasswordViewModel.isButtonEnabled.collectAsState()

    resetPasswordViewModel.toastMessage.observe(LocalLifecycleOwner.current, Observer { message ->
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    })


    val registerSuccess by resetPasswordViewModel.registerSuccess.collectAsState()

    val isLoading by resetPasswordViewModel.isLoading.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { }
    )

    if (registerSuccess) {
        AlertComponent(
            title = stringResource(R.string.message_reset_password_success_title),
            message = stringResource(R.string.message_reset_password_success_message),
            confirmText = stringResource(R.string.message_reset_password_success_confirm),
            cancelText = stringResource(R.string.message_reset_password_success_cancel),
            onConfirmClick = {
                resetPasswordViewModel.hideAlert()
                launcher.launch(handleGoToEmail())
            },
            onDismissClick = {
                resetPasswordViewModel.hideAlert()
            })
    }

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
            email,
            isButtonEnabled,
            onChangeEmail = {
                resetPasswordViewModel.updateEmail(it)
            },
            onRegisterClick = {
                resetPasswordViewModel.reset()

            }
        )
    }
    Loader(isLoading)

}


@Composable
private fun ContentRegisterView(
    paddingValues: PaddingValues,
    email: String,
    isButtonEnabled: Boolean,
    onChangeEmail: (String) -> Unit,
    onRegisterClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(Constants.SPACE_DEFAULT.dp)
    ) {


        InputRound(
            label = stringResource(R.string.label_email),
            value = email,
            placeholder = stringResource(R.string.label_email_placeholder), KeyboardType.Email,
            imeAction = ImeAction.Done
        ) {
            onChangeEmail(it)
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

        Button(
            enabled = isButtonEnabled,
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

private fun handleGoToEmail(): Intent {
    return Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_APP_EMAIL)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
