package com.jdosantos.gratitudewavev1.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.jdosantos.gratitudewavev1.ui.navigation.MainNavigation
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val splashViewModel: SplashViewModel by viewModels()

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
        /*    val userAuthState = mutableStateOf(UserAuthState.UNKNOWN)


            splashViewModel.onEvent(SplashEvent.CheckAuthentication)*/

        /*
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        splashViewModel.isAuthenticated.collect { userAuthState.value = it

                        Log.d("AUTH", "is: ${userAuthState.value}")}
                    }
                }
        */


        setContent {
            GratitudeWaveV1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(
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
