package com.jdosantos.gratitudewavev1.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.jdosantos.gratitudewavev1.ui.navigation.MainNavigation
import com.jdosantos.gratitudewavev1.ui.notification.NotificationWorker
import com.jdosantos.gratitudewavev1.ui.theme.GratitudeWaveV1Theme
import com.jdosantos.gratitudewavev1.ui.view.auth.login.LoginViewModel
import com.jdosantos.gratitudewavev1.ui.view.auth.register.RegisterViewModel
import com.jdosantos.gratitudewavev1.ui.view.auth.verify.VerifyEmailViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.goals.GoalsViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.home.HomeViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.home.bycalendar.ByCalendarViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.home.notesbytag.NotesByTagViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.home.progress.ProgressViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.home.search.SearchNoteViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.home.tags.SearchByTagsViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.note.detailnote.DetailNoteViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.note.updatenote.UpdateNoteViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.note.writenote.WriteNoteViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.notifications.NotificationsViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.profile.ProfileViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.profile.feedback.FeedbackViewModel
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginViewModel: LoginViewModel by viewModels()
        val writeNoteViewModel: WriteNoteViewModel by viewModels()
        val profileViewModel: ProfileViewModel by viewModels()
        val homeViewModel: HomeViewModel by viewModels()
        val detailNoteViewModel: DetailNoteViewModel by viewModels()
        val updateNoteViewModel: UpdateNoteViewModel by viewModels()
        val byCalendarViewModel: ByCalendarViewModel by viewModels()
        val registerViewModel: RegisterViewModel by viewModels()
        val verifyEmailViewModel: VerifyEmailViewModel by viewModels()
        val searchNoteViewModel: SearchNoteViewModel by viewModels()
        val searchByTagsViewModel: SearchByTagsViewModel by viewModels()
        val notesByTagViewModel: NotesByTagViewModel by viewModels()
        val progressViewModel: ProgressViewModel by viewModels()
        val goalsViewModel: GoalsViewModel by viewModels()
        val notificationsViewModel: NotificationsViewModel by viewModels()
        val settingsViewModel: SettingsViewModel by viewModels()
        val feedbackViewModel: FeedbackViewModel by viewModels()



        setContent {
            val context = LocalContext.current
            // Notificaciones
            var permission by remember {
                mutableStateOf(
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                )
            }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted -> permission = isGranted })

            LaunchedEffect(Unit) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

                NotificationWorker.releaseNotification(context)
            }

            GratitudeWaveV1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(
                        loginViewModel,
                        writeNoteViewModel,
                        profileViewModel,
                        homeViewModel,
                        detailNoteViewModel,
                        updateNoteViewModel,
                        byCalendarViewModel,
                        registerViewModel,
                        verifyEmailViewModel,
                        searchNoteViewModel,
                        searchByTagsViewModel,
                        notesByTagViewModel,
                        progressViewModel,
                        goalsViewModel,
                        notificationsViewModel,
                        settingsViewModel,
                        feedbackViewModel
                    )
                }
            }
        }
    }
}
