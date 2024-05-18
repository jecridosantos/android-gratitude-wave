package com.jdosantos.gratitudewavev1.utils

import android.content.res.Resources
import com.jdosantos.gratitudewavev1.domain.enums.DayOfWeek
import com.jdosantos.gratitudewavev1.domain.handles.ReminderRepetitions
import com.jdosantos.gratitudewavev1.utils.constants.Constants

fun getFirstLetters(selectedDays: List<Int>, resources: Resources): String {
    val daysOfWeek = DayOfWeek.entries.toTypedArray()
    val dayMap = daysOfWeek.associateBy { it.ordinal }

    return selectedDays.sorted().joinToString(", ") { index ->
        val day = dayMap[index]
        resources.getString(day!!.nameRes).take(3)
    }
}


fun checkSelectedDays(selectedDays: List<Int>): Pair<Boolean, Boolean> {
    val allDaysSelected = selectedDays.size == DayOfWeek.entries.size
    val weekdaysSelected = selectedDays.size == Constants.WORK_WEEK_SIZE && selectedDays.containsAll(
        listOf(
            DayOfWeek.MON.ordinal,
            DayOfWeek.TUE.ordinal,
            DayOfWeek.WED.ordinal,
            DayOfWeek.THU.ordinal,
            DayOfWeek.FRI.ordinal
        )
    )

    return Pair(allDaysSelected, weekdaysSelected)
}

fun getWeekDays(resources: Resources, fullNames: Boolean = true): List<String> {
    return enumValues<DayOfWeek>().map { day ->
        val dayName = resources.getString(day.nameRes)
        if (!fullNames) dayName.take(3) else dayName
    }
}

val repeatListOptions = listOf(
//    ReminderRepetitions.Once,
    ReminderRepetitions.Daily,
    ReminderRepetitions.MonToFri,
    ReminderRepetitions.Custom
)

fun getRepeatDescription(index: Int): Int{
    return repeatListOptions[index].title
}
