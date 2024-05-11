package com.jdosantos.gratitudewavev1.ui.view.auth.verify

import android.content.Context
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VERIFY_EMAIL_OPTION_GO_TO_EMAIL
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VERIFY_EMAIL_OPTION_GO_TO_LOGIN
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VERIFY_EMAIL_OPTION_RESEND_LINK

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailView(navController: NavController, verifyEmailViewModel: VerifyEmailViewModel) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { //Do something when userData comes back in app
        }
    )
    LaunchedEffect(Unit) {
        verifyEmailViewModel.init(context, navController)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.label_verify_email)) },
            )
        }

    ) { paddingValues ->
        ContentVerifyEmailView(
            paddingValues,
            navController,
            verifyEmailViewModel,
            onGoToEmail = {
                launcher.launch(handleGoToEmail())
            },
            onResendLink = {
                handleResendLink(verifyEmailViewModel, context)
            },
            onGoToLogin = {
                handleGoToLogin(verifyEmailViewModel, navController)
            }
        )
    }
}

@Composable
fun ContentVerifyEmailView(
    paddingValues: PaddingValues,
    navController: NavController,
    verifyEmailViewModel: VerifyEmailViewModel,
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
        val email = Firebase.auth.currentUser?.email
        Text(
            text = stringResource(R.string.label_message_verify_email_with_current_email, email!!),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
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
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
        )
        Button(
            onClick = {
                navController.navigate("SplashView")
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
fun TaskMenu(
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

private fun handleGoToLogin(
    verifyEmailViewModel: VerifyEmailViewModel,
    navController: NavController
) {
    verifyEmailViewModel.logout {

        navController.navigate("LoginView") {

            popUpTo("SplashView") { inclusive = true }
        }
    }
}

private fun handleResendLink(verifyEmailViewModel: VerifyEmailViewModel, context: Context) {
    verifyEmailViewModel.resendLink { success ->
        val message =
            context.getString(if (success) R.string.label_resend_link_success else R.string.label_resend_link_error)
        Toast.makeText(
            context, message, Toast.LENGTH_LONG
        ).show()

    }
}