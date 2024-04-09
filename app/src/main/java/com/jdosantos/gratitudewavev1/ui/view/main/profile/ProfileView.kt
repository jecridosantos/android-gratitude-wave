package com.jdosantos.gratitudewavev1.ui.view.main.profile

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.ui.widget.ConfigItem
import com.jdosantos.gratitudewavev1.ui.widget.Title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(profileViewModel: ProfileViewModel, navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = stringResource(R.string.label_title_profile)) })
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

    LaunchedEffect(Unit) {
        profileViewModel.getCuurrentUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {

        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                HeaderProfile(profileViewModel)

                Box(modifier = Modifier.padding(16.dp)) {
                    Title(text = "Ajustes", modifier = Modifier)
                }


                Column {
                    ItemSettings(Icons.Default.Settings, null, "Settings") {
                        navController.navigate("SettingsView")
                    }
                    ItemSettings(Icons.Default.Email, null, "Send feedback") {
                        navController.navigate("FeedbackView")
                    }
                    ItemSettings(null, R.drawable.baseline_help_outline_24, "Help Center") {
                        navController.navigate("HelpView")
                    }
                    ItemSettings(null, null, "Privacy Policy") {}
                    ItemSettings(null, null, "Terms of Service") {}
                    Spacer(modifier = Modifier.height(16.dp))
                }


                Spacer(modifier = Modifier.weight(1f))

                Box(modifier = Modifier.padding(16.dp)) {
                    ElevatedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            profileViewModel.logout {

                                // navController.navigate("LoginView")

                                navController.navigate("LoginView") {

                                    popUpTo("SplashView") { inclusive = true }
                                }
                            }
                        },
                        // shape = MaterialTheme.shapes.extraSmall,
                        // border = ButtonDefaults.outlinedButtonBorder
                    ) {
                        Text(text = stringResource(R.string.label_log_out))
                    }
                }

            }

        }


    }
}

@Composable
private fun HeaderProfile(profileViewModel: ProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (profileViewModel.user.photoUrl != null) {
                Image(

                    painter = rememberImagePainter(data = profileViewModel.user.photoUrl),
                    contentDescription = null, // Add appropriate content description
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(

                    painter = painterResource(id = R.drawable.hombre),
                    contentDescription = null, // Add appropriate content description
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = profileViewModel.user.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = profileViewModel.user.email,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.tertiary
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
            Text(text = title, modifier = Modifier.weight(1f))

            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "", tint = MaterialTheme.colorScheme.outline)

        }
    }
}