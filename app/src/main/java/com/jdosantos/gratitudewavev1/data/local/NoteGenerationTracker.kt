package com.jdosantos.gratitudewavev1.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.edit
import com.jdosantos.gratitudewavev1.domain.handles.NoteGenerationResult
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsAccount.Companion.MAX_CONSECUTIVE_NOTES
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsAccount.Companion.MAX_NOTES_PER_DAY
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsAccount.Companion.WAIT_TIME_MINUTES
import java.util.Calendar
import javax.inject.Inject

class NoteGenerationTracker @Inject constructor(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("note_generation_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LAST_DATE = "last_date"
        private const val KEY_TOTAL_NOTE_COUNT = "total_note_count"
        private const val KEY_CONSECUTIVE_NOTE_COUNT = "consecutive_note_count"
        private const val KEY_LAST_GENERATION_TIME = "last_generation_time"
    }

    fun canGenerateNote(): NoteGenerationResult {
        val currentDate = getCurrentDate()
        val lastDate = preferences.getString(KEY_LAST_DATE, "")

        if (currentDate != lastDate) {
            resetDailyCount(currentDate)
            return NoteGenerationResult.CanGenerate
        } else {
            val totalNoteCount = preferences.getInt(KEY_TOTAL_NOTE_COUNT, 0)
            val consecutiveNoteCount = preferences.getInt(KEY_CONSECUTIVE_NOTE_COUNT, 0)
            val lastGenerationTime = preferences.getLong(KEY_LAST_GENERATION_TIME, 0L)

            return when {
                totalNoteCount >= MAX_NOTES_PER_DAY -> NoteGenerationResult.DailyLimitReached
                consecutiveNoteCount >= MAX_CONSECUTIVE_NOTES -> {
                    val waitTime =
                        WAIT_TIME_MINUTES * 60 * 1000 - (System.currentTimeMillis() - lastGenerationTime)
                    if (waitTime > 0) {
                        NoteGenerationResult.MustWait(waitTime)
                    } else {
                        NoteGenerationResult.CanGenerate
                    }
                }

                else -> NoteGenerationResult.CanGenerate
            }
        }
    }

    fun incrementNoteCount() {
        val currentDate = getCurrentDate()
        val lastDate = preferences.getString(KEY_LAST_DATE, "")
        val editor = preferences.edit()

        if (currentDate != lastDate) {
            resetDailyCount(currentDate)
        }

        val totalNoteCount = preferences.getInt(KEY_TOTAL_NOTE_COUNT, 0) + 1
        var consecutiveNoteCount = preferences.getInt(KEY_CONSECUTIVE_NOTE_COUNT, 0) + 1

        if (consecutiveNoteCount > MAX_CONSECUTIVE_NOTES && hasWaitedEnough(
                preferences.getLong(
                    KEY_LAST_GENERATION_TIME,
                    0L
                )
            )
        ) {
            consecutiveNoteCount = 1
        }

        editor.putInt(KEY_TOTAL_NOTE_COUNT, totalNoteCount)
        editor.putInt(KEY_CONSECUTIVE_NOTE_COUNT, consecutiveNoteCount)
        editor.putLong(KEY_LAST_GENERATION_TIME, System.currentTimeMillis())
        editor.apply()
    }

    private fun resetDailyCount(currentDate: String) {
        val editor = preferences.edit()
        editor.putString(KEY_LAST_DATE, currentDate)
        editor.putInt(KEY_TOTAL_NOTE_COUNT, 0)
        editor.putInt(KEY_CONSECUTIVE_NOTE_COUNT, 0)
        editor.putLong(KEY_LAST_GENERATION_TIME, 0L)
        editor.apply()
    }

    private fun hasWaitedEnough(lastGenerationTime: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val waitTimeMillis = WAIT_TIME_MINUTES * 60 * 1000
        return currentTime - lastGenerationTime >= waitTimeMillis
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$year-$month-$day"
    }

    fun clearAll() {
        preferences.edit().clear().apply()
    }
}
