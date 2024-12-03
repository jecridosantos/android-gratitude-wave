package com.jdosantos.gratitudewavev1.ui.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.data.local.RemindersStore
import com.jdosantos.gratitudewavev1.domain.handles.ReminderRepetitions
import com.jdosantos.gratitudewavev1.domain.models.UserSettingReminders
import com.jdosantos.gratitudewavev1.domain.services.impl.DefaultLanguageProvider
import com.jdosantos.gratitudewavev1.ui.MainActivity
import com.jdosantos.gratitudewavev1.utils.constants.Constants
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

class NotificationWorker @Inject constructor(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val calendar = Calendar.getInstance()
        val dayOfWeek = getAdjustedDayOfWeek(calendar)

        if (shouldShowNotification(dayOfWeek)) {
            val reminderTags = getReminderTags()
            if (reminderTags.isNotEmpty()) {
                showNotification(reminderTags[0])
            }
        }

        return Result.success()
    }

    private fun getAdjustedDayOfWeek(calendar: Calendar): Int {
        return (calendar.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY + 7) % 7
    }

    private fun shouldShowNotification(dayOfWeek: Int): Boolean {
        return !tags.any { it.startsWith("DAY_") } || tags.contains("DAY_$dayOfWeek")
    }

    private fun getReminderTags(): List<String> {
        return tags.filter { it.startsWith("DETAIL_") }
    }

    private fun showNotification(reminderTag: String) {
        val notification = buildNotification(reminderTag)
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(Random.nextInt(), notification)
    }

    private fun buildNotification(reminderTag: String): Notification {

        val reminderDetail = reminderTag.substringAfter("DETAIL_")
        val label = reminderDetail.split("|")[1]

        val languageProvider = DefaultLanguageProvider(context)
        val messages = LocalizedMessageNotificationManager(languageProvider)

        fun getRandomTitle() = messages.getLocalizedTitles().random()
        fun getRandomMessage() = messages.getLocalizedMessages().random()

        val title = label.ifEmpty { getRandomTitle() }

        val pendingIntent = createPendingIntent(applicationContext, "gratitude_view")


        return NotificationCompat.Builder(applicationContext, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(getRandomMessage())
            .setSmallIcon(R.drawable.ic_stat_name)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun createPendingIntent(context: Context, route: String): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("destination_route", route)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        // Constantes para tags y claves de trabajos
        private const val KEY_PREFIX = "KEY"
        private const val TAG_PREFIX_DETAIL = "DETAIL"

        suspend fun releaseNotification(context: Context) {
            val store = RemindersStore(context)

            store.getReminders().collect { reminderSet ->
                reminderSet?.forEach { reminderString ->
                    val reminder = parseReminderString(reminderString)
                    if (reminder != null) {
                        scheduleNotification(context, reminder)
                    }

                }
            }
        }

        private fun parseReminderString(reminderString: String): UserSettingReminders? {
            val parts = reminderString.split("|")
            val time = parts[1].split(",")
            return if (parts.size == 6) {
                UserSettingReminders(
                    uuid = parts[0],
                    hour = time[0].toIntOrNull(),
                    minute = time[1].toIntOrNull(),
                    label = parts[2],
                    repeat = parts[3].toInt(),
                    repeatDays = parts[4].split(",").mapNotNull { it.toIntOrNull() }
                        .toMutableList(),
                    active = parts[5].toBoolean()
                )
            } else {
                null;
            }

        }

        private fun scheduleNotification(context: Context, reminder: UserSettingReminders) {
            val tagKey = "${KEY_PREFIX}_${reminder.uuid!!}"
            val tagReminder = "${TAG_PREFIX_DETAIL}_${reminder.toString().substringAfter("|")}"
            val workManager = WorkManager.getInstance(context)
            val workInfos = workManager.getWorkInfosByTag(tagKey).get()
            var createNewWork = true

            workInfos.forEach { workInfo ->
                val found = workInfo.tags.contains(tagReminder)
                if (!found) {
                    createNewWork = true
                    workManager.cancelWorkById(workInfo.id)
                    workManager.pruneWork()
                } else {
                    createNewWork = false
                    return@forEach
                }
            }

            if (createNewWork && reminder.active && reminder.hour != null && reminder.minute != null) {
                val notificationTime = getNotificationTime(reminder)
                if (notificationTime.after(Calendar.getInstance())) {
                    val delayInMillis =
                        notificationTime.timeInMillis - Calendar.getInstance().timeInMillis
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                        .setRequiresCharging(false)
                        .setRequiresBatteryNotLow(false)
                        .build()

                    val notificationWork = when (reminder.repeat) {
                        ReminderRepetitions.Daily.id -> PeriodicWorkRequestBuilder<NotificationWorker>(
                            repeatInterval = 1,
                            repeatIntervalTimeUnit = TimeUnit.DAYS
                        )

                        ReminderRepetitions.MonToFri.id -> {
                            val workBuilder = PeriodicWorkRequestBuilder<NotificationWorker>(
                                repeatInterval = 1,
                                repeatIntervalTimeUnit = TimeUnit.DAYS
                            )
                            for (i in 0..4) {
                                workBuilder.addTag("DAY_$i")
                            }
                            workBuilder
                        }

                        ReminderRepetitions.Custom.id -> {
                            if (!reminder.repeatDays.isNullOrEmpty()) {
                                val workBuilder = PeriodicWorkRequestBuilder<NotificationWorker>(
                                    repeatInterval = 1,
                                    repeatIntervalTimeUnit = TimeUnit.DAYS
                                )
                                reminder.repeatDays.forEach { day ->
                                    workBuilder.addTag("DAY_$day")
                                }
                                workBuilder
                            } else {
                                OneTimeWorkRequestBuilder<NotificationWorker>()
                            }
                        }

                        else -> OneTimeWorkRequestBuilder<NotificationWorker>()
                    }
                    WorkManager.getInstance(context).enqueue(
                        notificationWork
                            .addTag(tagKey)
                            .addTag(tagReminder)
                            .setConstraints(constraints)
                            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
                            .build()
                    )
                }
            }
        }

        private fun getNotificationTime(reminder: UserSettingReminders): Calendar {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, reminder.hour!!)
                set(Calendar.MINUTE, reminder.minute!!)
                set(Calendar.SECOND, 0)
            }
        }

    }
}
