package com.jdosantos.gratitudewavev1

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.jdosantos.gratitudewavev1.utils.constants.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GratitudeWaveV1Application : Application() {

    // Notificaciones
    override fun onCreate() {
        super.onCreate()

        val notificationChannel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationChannel.description = Constants.NOTIFICATION_CHANNEL_DESCRIPTION

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}