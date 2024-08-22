package com.jdosantos.gratitudewavev1.ui.view.main.home.progress

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.domain.states.ProgressState
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetNoteCreationDatesByEmailUseCase
import com.jdosantos.gratitudewavev1.utils.uniqueDates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val getNoteCreationDatesByEmailUseCase: GetNoteCreationDatesByEmailUseCase
) : ViewModel() {
    private val tag = this::class.java.simpleName
    private val _dates = MutableStateFlow(listOf(Date()))

    var progressState by mutableStateOf(ProgressState())
        private set

    fun initialize() {
        getNoteCreationDatesByEmailUseCase
            .execute(
                callback = { dates ->
                    _dates.value = dates
                    calculateStreaks()
                },
                onError = {
                    Log.e(tag, "initialize - getNoteCreationDatesByEmailUseCase")
                }
            )
    }


    private fun calculateStreaks() {
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }
        val initialDates = _dates.value
        val dates = uniqueDates(initialDates)


        var currentStreak = 0
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        var tempDate = today.time

        while (dates.contains(tempDate)) {
            currentStreak++
            tempDate = Calendar.getInstance().apply {
                time = tempDate
                add(Calendar.DAY_OF_MONTH, -1)
            }.time
        }

        // Calcula la mejor racha
        var bestStreak = 0
        var bestStreakStart: Date? = null
        var bestStreakEnd: Date? = null
        var tempStreak = 0
        var tempStreakStart: Date? = null
        var currentCurrent: Date? = null

        for (date in dates) {
            if (currentCurrent == null) {
                tempStreakStart = date
                currentCurrent = date
                tempStreak = 1
            } else {
                val nextDay = Calendar.getInstance().apply {
                    time = tempStreakStart!!
                    add(Calendar.DAY_OF_MONTH, 1)
                }.time
                if (date == nextDay) {
                    tempStreak++
                    tempStreakStart = date
                } else {
                    if (tempStreak > bestStreak) {
                        bestStreak = tempStreak
                        bestStreakStart = currentCurrent
                        bestStreakEnd = tempStreakStart
                    }
                    tempStreak = 1
                    tempStreakStart = date
                    currentCurrent = null
                }
            }
        }

        if (tempStreak > bestStreak) {
            bestStreak = tempStreak
            bestStreakStart = tempStreakStart
            bestStreakEnd = dates.last()
        }

        progressState = progressState.copy(
            totalNotes = initialDates.size,
            currentStreak = currentStreak,
            bestStreak = bestStreak

        )
    }
}