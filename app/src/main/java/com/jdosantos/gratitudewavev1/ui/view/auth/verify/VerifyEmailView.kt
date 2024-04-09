package com.jdosantos.gratitudewavev1.ui.view.auth.verify

import android.content.Intent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailView(navController: NavController, verifyEmailViewModel: VerifyEmailViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Verificar correo") },
                /*               navigationIcon = {
                                   IconButton(onClick = {
                                       navController.popBackStack()
                                   }) {
                                       Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                                   }

                               }*/
            )
        }

    ) { paddingValues ->
        ContentVerifyEmailView(paddingValues, navController, verifyEmailViewModel)
    }
}

@Composable
fun ContentVerifyEmailView(
    paddingValues: PaddingValues,
    navController: NavController,
    verifyEmailViewModel: VerifyEmailViewModel
) {
    val context = LocalContext.current
    var taskMenuOpen by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { //Do something when user comes back in app
             }
            )


    LaunchedEffect(Unit) {
        verifyEmailViewModel.init(context, navController)
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val email = Firebase.auth.currentUser?.email
        Text(
            text = "We sent an email to  ${email} with a link to verify your email.",
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
            text = "Can’t see it? Try your spam folder or ask us to resend the link.",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        Button(
            onClick = {
                navController.navigate("SplashView")
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            Text(text = "Actualizar")
        }

        TextButton(
            onClick = {

                taskMenuOpen = true

            }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Más opciones")

            TaskMenu(
                expanded = taskMenuOpen,
                onItemClick = {

                    when (it) {
                        0 -> {
                    /*        val emailAppIntent = Intent(Intent.ACTION_CHOOSER)
                            emailAppIntent.addCategory(Intent.CATEGORY_APP_EMAIL)
                            context.startActivity(emailAppIntent)*/

                            val intent = Intent(Intent.ACTION_MAIN).apply {
                                addCategory(Intent.CATEGORY_APP_EMAIL)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            launcher.launch(intent)

                        }
                        1 -> {
                            verifyEmailViewModel.resendLink(context)
                        }
                        2 -> {
                            verifyEmailViewModel.logout {

                                // navController.navigate("LoginView")

                                navController.navigate("LoginView") {

                                    popUpTo("SplashView") { inclusive = true }
                                }
                            }
                        }
                        else -> {
                            // Opción por defecto
                        }
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
    expanded: Boolean, // (1)
    onItemClick: (Int) -> Unit,
    onDismiss: () -> Unit
) {

    val options = listOf( // (2)
        "Ir a mi correo",
        "Volver a enviar link",
        "Volver al inicio"
    )

    DropdownMenu( // (3)
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