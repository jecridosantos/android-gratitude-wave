package com.jdosantos.gratitudewavev1.ui.view.main.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import com.jdosantos.gratitudewavev1.ui.widget.ConfigItem
import com.jdosantos.gratitudewavev1.ui.widget.TextItem
import com.jdosantos.gratitudewavev1.ui.widget.Title
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.POLICIES_URL
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MIN
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.TERMS_URL
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VERSION_APP

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = stringResource(R.string.label_title_profile))

            })
        }
    ) {
        ContentProfileView(paddingValues = it, profileViewModel, navController)
    }
}

@Composable
fun ContentProfileView(
    paddingValues: PaddingValues,
    profileViewModel: ProfileViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        profileViewModel.getCurrentUser()
    }

    profileViewModel.toastMessage.observe(LocalLifecycleOwner.current, Observer { message ->
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    })

    val logoutSuccess by profileViewModel.logoutSuccess.collectAsState()
    LaunchedEffect(logoutSuccess) {
        if (logoutSuccess) {
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    HeaderProfile(profileViewModel)

                    Box(modifier = Modifier.padding(SPACE_DEFAULT.dp)) {
                        Title(
                            text = stringResource(id = R.string.label_settings),
                            modifier = Modifier
                        )
                    }


                    Column {
                        ItemSettings(
                            Icons.Default.Face,
                            null,
                            stringResource(id = R.string.label_me)
                        ) {
                            navController.navigate(Screen.OnboardingScreen.route)
                        }

                        ItemSettings(
                            Icons.Default.Settings,
                            null,
                            stringResource(id = R.string.label_settings)
                        ) {
                            navController.navigate(Screen.SettingsScreen.route)
                        }
                        ItemSettings(
                            Icons.Default.Email, null,
                            stringResource(R.string.label_send_feedback)
                        ) {
                            navController.navigate(Screen.FeedbackScreen.route)
                        }
                        ItemSettings(
                            null, R.drawable.baseline_help_outline_24,
                            stringResource(R.string.label_help_center)
                        ) {
                            navController.navigate(Screen.HelpScreen.route)
                        }
                        ItemSettings(null, null, stringResource(R.string.label_privacy_policy)) {
//                            val openURL = Intent(Intent.ACTION_VIEW)
//                            openURL.data =
//                                Uri.parse(POLICIES_URL)
//                            context.startActivity(openURL)

                            //   profileViewModel.onShowPolicies();
                            navController.navigate(
                                Screen.WebViewScreen.params(
                                    R.string.label_privacy_policy,
                                    POLICIES_URL
                                )
                            )
                        }
                        ItemSettings(null, null, stringResource(R.string.label_terms_of_service)) {
//                            val openURL = Intent(Intent.ACTION_VIEW)
//                            openURL.data =
//                                Uri.parse(Constants.TERMS_URL)
//                            context.startActivity(openURL)
                            navController.navigate(
                                Screen.WebViewScreen.params(
                                    R.string.label_terms_of_service,
                                    TERMS_URL
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }


                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = VERSION_APP, modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                SPACE_DEFAULT_MIN.dp
                            ),
                        textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall
                    )
                    Box(modifier = Modifier.padding(SPACE_DEFAULT.dp)) {
                        ElevatedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { profileViewModel.logout() },
                        ) {
                            Text(text = stringResource(R.string.label_log_out))
                        }
                    }


                }

            }
        }
    }
}

@Composable
private fun HeaderProfile(profileViewModel: ProfileViewModel) {


    val userName by profileViewModel.userName.collectAsState()
    val userEmail by profileViewModel.userEmail.collectAsState()
    val userAvatar by profileViewModel.userAvatar.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SPACE_DEFAULT.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userAvatar)
                    .build()
            )
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(SPACE_DEFAULT.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = userEmail,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

    }
}

@Composable
private fun ItemSettings(
    icon: ImageVector?,
    icon2: Int?,
    title: String,
    onClick: () -> Unit
) {
    ConfigItem(onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = "")
                Spacer(modifier = Modifier.width(24.dp))
            }
            if (icon2 != null) {
                Icon(painter = painterResource(icon2), contentDescription = "")
                Spacer(modifier = Modifier.width(24.dp))
            }
            TextItem(text = title, modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.outline
            )

        }
    }
}