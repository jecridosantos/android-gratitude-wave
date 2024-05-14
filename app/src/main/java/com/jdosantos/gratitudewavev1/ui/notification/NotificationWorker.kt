package com.jdosantos.gratitudewavev1.ui.notification

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.data.local.RemindersStore
import com.jdosantos.gratitudewavev1.domain.handles.ReminderRepetitions
import com.jdosantos.gratitudewavev1.domain.models.UserSettingReminders
import com.jdosantos.gratitudewavev1.ui.view.main.profile.settings.SettingsViewModel
import com.jdosantos.gratitudewavev1.utils.constants.Constants
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        showBasicNotification()
        return Result.success()
    }

    private fun showBasicNotification() {
        val notification =
            NotificationCompat.Builder(applicationContext, Constants.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Titulo de notificacion")
                .setContentText("Lorem ipsum dolor sit amet.")
                .setSmallIcon(R.drawable.baseline_emoji_emotions_24)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setAutoCancel(true)
                .build()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }

    companion object {

        suspend fun releaseNotification(context: Context, settingsViewModel: SettingsViewModel) {
            val store = RemindersStore(context)

            store.getReminders().collect { reminderSet ->
                reminderSet!!.forEachIndexed { index, reminderString ->
                    val reminder = parseReminderString(reminderString)
                    Log.e("REMINDER", reminder.toString())
                    scheduleNotification(context, reminder)

                    if (reminder.repeat.toInt() == ReminderRepetitions.Once.id) {
                     //   val updatedReminderSet = reminderSet.minus(reminderString)

                       // store.saveRemindersSet(updatedReminderSet)
                        //settingsViewModel.disableReminder(index)
                    }


                }


            }
        }

        private fun parseReminderString(reminderString: String): UserSettingReminders {
            Log.e("REMINDER", reminderString)
            val parts = reminderString.split("|")
            val time = parts[0].split(",")
            return UserSettingReminders(
                hour = time[0].toIntOrNull(),
                minute = time[1].toIntOrNull(),
                label = parts[1],
                repeat = parts[2].toInt(),
                active = parts[3].toBoolean(),
                repeatDays = parts[4].split(",").mapNotNull { it.toIntOrNull() }.toMutableList()
            )
        }

        private fun scheduleNotification(context: Context, reminder: UserSettingReminders) {
            if (reminder.active && reminder.hour != null && reminder.minute != null) {
                val currentTime = Calendar.getInstance()
                val notificationTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, reminder.hour)
                    set(Calendar.MINUTE, reminder.minute)
                    set(Calendar.SECOND, 0)
                }

                if (notificationTime.after(currentTime)) {
                    val delayInMillis = notificationTime.timeInMillis - currentTime.timeInMillis

                    Log.e("REMINDER", "delayInMillis $delayInMillis")
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                        .setRequiresCharging(false)
                        .setRequiresBatteryNotLow(false)
                        .build()

                    val notificationWork = OneTimeWorkRequestBuilder<NotificationWorker>()
                        .setConstraints(constraints)
                        .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
                        .build()

                    WorkManager.getInstance(context).enqueue(notificationWork)
                }
            }
        }
    }
}