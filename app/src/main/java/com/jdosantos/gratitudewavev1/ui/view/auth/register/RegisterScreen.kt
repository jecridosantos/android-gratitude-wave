package com.jdosantos.gratitudewavev1.ui.view.auth.register

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.ui.widget.InputRound
import com.jdosantos.gratitudewavev1.ui.widget.Loader
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jdosantos.gratitudewavev1.ui.widget.TooltipPopup
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.POLICIES_URL
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.TERMS_URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val name by registerViewModel.name.collectAsState()
    val email by registerViewModel.email.collectAsState()
    val password by registerViewModel.password.collectAsState()
    val isButtonEnabled by registerViewModel.isButtonEnabled.collectAsState()

    registerViewModel.toastMessage.observe(LocalLifecycleOwner.current, Observer { message ->
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    })

    val registerSuccess by registerViewModel.registerSuccess.collectAsState()

    val isLoading by registerViewModel.isLoading.collectAsState()

    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            navController.navigate(Screen.VerifyEmailScreen.route)
        }
    }

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
            context,
            paddingValues,
            name,
            email,
            password,
            isButtonEnabled,
            onNameChange = { registerViewModel.updateName(it) },
            onEmailChange = { registerViewModel.updateEmail(it) },
            onPasswordChange = { registerViewModel.updatePassword(it) },
            onRegisterClick = { registerViewModel.register() }
        )
    }

    Loader(isLoading)

}

@Composable
private fun ContentRegisterView(
    context: Context,
    paddingValues: PaddingValues,
    name: String,
    email: String,
    password: String,
    isButtonEnabled: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(SPACE_DEFAULT.dp)
            .verticalScroll(rememberScrollState())

    ) {


        InputRound(
            label = stringResource(R.string.label_name),
            value = name,
            placeholder = stringResource(R.string.label_name_placeholder),
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ) {
            onNameChange(it)
        }

        InputRound(
            label = stringResource(R.string.label_email),
            value = email,
            placeholder = stringResource(R.string.label_email_placeholder),
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ) {
            onEmailChange(it)
        }

        InputRound(
            label = stringResource(R.string.label_password),
            value = password,
            placeholder = stringResource(R.string.label_password_placeholder),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ) {
            onPasswordChange(it)
        }

        PasswordRules()

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(id = R.string.label_message_accept_conditions),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
        )
        Text(text = stringResource(R.string.label_privacy_policy),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
                .clickable {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data =
                    Uri.parse(POLICIES_URL)
                context.startActivity(openURL)
            })
        Text(text = stringResource(R.string.label_terms_of_service),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
                .clickable {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data =
                    Uri.parse(TERMS_URL)
                context.startActivity(openURL)
            })
        Button(
            enabled = isButtonEnabled,
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

@Composable
private fun PasswordRules(
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.label_secure_password),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = SPACE_DEFAULT.dp)
        )
        TooltipPopup(
            modifier = Modifier
                .padding(start = 8.dp),
            requesterView = { modifier ->
                Icon(
                    modifier = modifier,
                    imageVector = Icons.Default.Info,
                    contentDescription = "TooltipPopup",
                    tint = Color.Gray,
                )
            },
            tooltipContent = {
                Column(
                    Modifier.padding(top = SPACE_DEFAULT.dp, bottom = SPACE_DEFAULT.dp)
                ) {
                    Text(
                        text = stringResource(R.string.password_rule_length),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp),
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.password_rule_uppercase),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp),
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.password_rule_number),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp),
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.password_rule_no_whitespace),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp),
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.password_rule_no_name_email),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp),
                        color = Color.White
                    )
                }
            }
        )
    }
}