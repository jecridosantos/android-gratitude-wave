package com.jdosantos.gratitudewavev1.ui.view.main.home.progress

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.domain.models.Goals
import com.jdosantos.gratitudewavev1.domain.states.ProgressState
import com.jdosantos.gratitudewavev1.domain.usecase.goals.GetGoalsByUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetNoteCreationDatesByEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val getNoteCreationDatesByEmailUseCase: GetNoteCreationDatesByEmailUseCase,
    private val getGoalsByUserUseCase: GetGoalsByUserUseCase
) : ViewModel() {
    private val tag = this::class.java.simpleName
    private val _dates = MutableStateFlow(listOf(Date()))

    val goals = MutableStateFlow(Goals())

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

        getGoalsByUserUseCase.execute(
            callback = { goals.value = it },
            onError = {
                Log.e(tag, "initialize - getGoalsByUserUseCase")
            }
        )
    }

    private fun uniqueDates(dates: List<Date>): List<Date> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val uniqueDatesSet = dates.map { date ->
            // Convierte Date a String con formato "yyyy-MM-dd"
            dateFormat.format(date)
        }.toSet() // Utiliza un conjunto para eliminar duplicados

        return uniqueDatesSet.map { dateString ->
            // Convierte String de vuelta a Date
            Calendar.getInstance().apply {
                time = dateFormat.parse(dateString)!!
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
        }
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

        // Verifica si la última racha es la mejor
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