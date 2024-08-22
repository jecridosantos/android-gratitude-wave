package com.jdosantos.gratitudewavev1.ui.view.auth.verify

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VERIFY_EMAIL_OPTION_GO_TO_EMAIL
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VERIFY_EMAIL_OPTION_GO_TO_LOGIN
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VERIFY_EMAIL_OPTION_RESEND_LINK

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailScreen(
    navController: NavController,
    verifyEmailViewModel: VerifyEmailViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { }
    )

    LaunchedEffect(Unit) {
        verifyEmailViewModel.getCurrentUser()
    }

    verifyEmailViewModel.toastMessage.observe(LocalLifecycleOwner.current, Observer { message ->
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    })
    val emailCurrentUser by verifyEmailViewModel.emailCurrentUser.collectAsState()
    val registerSuccess by verifyEmailViewModel.registerSuccess.collectAsState()
    val logoutSuccess by verifyEmailViewModel.logoutSuccess.collectAsState()

    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            navController.navigate(Screen.ContainerScreen.route) {
                popUpTo(Screen.VerifyEmailScreen.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(logoutSuccess) {
        if (logoutSuccess) {
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(Screen.VerifyEmailScreen.route) { inclusive = true }
            }
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.label_verify_email)) },
            )
        }

    ) { paddingValues ->
        ContentVerifyEmailView(
            emailCurrentUser,
            paddingValues,
            onUpdate = {
                verifyEmailViewModel.reauthenticate()
            },
            onGoToEmail = {
                launcher.launch(handleGoToEmail())
            },
            onResendLink = {
                verifyEmailViewModel.resendLink()
            },
            onGoToLogin = {
                verifyEmailViewModel.logout()
            }
        )
    }
}

@Composable
private fun ContentVerifyEmailView(
    emailCurrentUser: String,
    paddingValues: PaddingValues,
    onUpdate: () -> Unit,
    onGoToEmail: () -> Unit,
    onResendLink: () -> Unit,
    onGoToLogin: () -> Unit,
) {
    var taskMenuOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(SPACE_DEFAULT.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                R.string.label_message_verify_email_with_current_email,
                emailCurrentUser
            ),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 50.dp, end = 50.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check, contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
        }

        Text(
            text = stringResource(R.string.label_verify_email_retry),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
        )
        Button(
            onClick = {
                onUpdate()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp, top = SPACE_DEFAULT.dp)
        ) {
            Text(text = stringResource(R.string.label_update))
        }

        TextButton(
            onClick = {
                taskMenuOpen = true
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(SPACE_DEFAULT.dp)
        ) {
            Text(text = stringResource(R.string.label_more_options))

            TaskMenu(
                expanded = taskMenuOpen,
                onItemClick = { selectedOption ->
                    when (selectedOption) {
                        VERIFY_EMAIL_OPTION_GO_TO_EMAIL -> onGoToEmail()
                        VERIFY_EMAIL_OPTION_RESEND_LINK -> onResendLink()
                        VERIFY_EMAIL_OPTION_GO_TO_LOGIN -> onGoToLogin()
                    }
                },
                onDismiss = {
                    taskMenuOpen = false
                }
            )
        }
    }
}

@Composable
private fun TaskMenu(
    expanded: Boolean,
    onItemClick: (Int) -> Unit,
    onDismiss: () -> Unit
) {

    val options = listOf(
        stringResource(R.string.label_go_to_email),
        stringResource(R.string.label_resend_link),
        stringResource(R.string.label_back_to_top)
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        options.forEachIndexed { index, option ->
            DropdownMenuItem(text = { Text(text = option) },
                onClick = {
                    onItemClick(index)
                    onDismiss()
                })
        }

    }
}

private fun handleGoToEmail(): Intent {
    return Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_APP_EMAIL)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
