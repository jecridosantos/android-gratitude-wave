package com.jdosantos.gratitudewavev1.ui.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.utils.constants.Constants
import kotlin.random.Random

class NotificationService(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun showBasicNotification() {
        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Momento de agradecer")
            .setContentText("Es un buen momento para escribir una nota de grtitud")
            .setSmallIcon(R.drawable.baseline_emoji_emotions_24)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}