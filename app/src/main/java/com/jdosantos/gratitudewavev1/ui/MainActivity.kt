package com.jdosantos.gratitudewavev1.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.jdosantos.gratitudewavev1.ui.navigation.MainNavigation
import com.jdosantos.gratitudewavev1.ui.notification.NotificationWorker
import com.jdosantos.gratitudewavev1.ui.theme.GratitudeWaveV1Theme
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.SettingsViewModel
import com.jdosantos.gratitudewavev1.utils.convertMillisToDateTime
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingsViewModel: SettingsViewModel by viewModels()
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

                NotificationWorker.releaseNotification(context, settingsViewModel)
            }

            val workInfosLiveData = getAllScheduledWork(context)

            workInfosLiveData.observe(this, Observer { workInfos ->
                for (workInfo in workInfos) {
                    val workId = workInfo.id
                    val state = workInfo.state
                    val tags = workInfo.tags
                    val nextScheduleTimeMillis =
                        convertMillisToDateTime(workInfo.nextScheduleTimeMillis)

                    Log.d("WorkInfo", "Work ID: $workId, State: $state, Tags: $tags, nextScheduleTimeMillis: $nextScheduleTimeMillis")
                }
            })

            GratitudeWaveV1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(settingsViewModel)
                }
            }
        }
    }

    fun getAllScheduledWork(context: Context): LiveData<List<WorkInfo>> {
        val workManager = WorkManager.getInstance(context)
        val workQuery = WorkQuery.Builder.fromStates(
            listOf(
                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING,
                WorkInfo.State.SUCCEEDED,
                WorkInfo.State.FAILED,
                WorkInfo.State.BLOCKED,
                WorkInfo.State.CANCELLED
            )
        ).build()

        return workManager.getWorkInfosLiveData(workQuery)
    }
}
