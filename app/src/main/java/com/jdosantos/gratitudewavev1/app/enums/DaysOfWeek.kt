package com.jdosantos.gratitudewavev1.app.enums

import java.util.Locale

enum class DayOfWeek(val index: Int, val abbreviationES: String, val abbreviationEN: String) {
    MON(0, "Lun", "Mon"),
    TUE(1, "Mar", "Tue"),
    WED(2, "Mié", "Wed"),
    THU(3, "Jue", "Thu"),
    FRI(4, "Vie", "Fri"),
    SAT(5, "Sáb", "Sat"),
    SUN(6, "Dom", "Sun")
}

fun getFirstLetters(selectedDays: List<Int>, locale: Locale = Locale.getDefault()): String {
    val daysOfWeek = DayOfWeek.entries.toTypedArray()
    val dayMap = daysOfWeek.associateBy { it.index }

    return selectedDays.joinToString(", ") { index ->
        val day = dayMap[index]
        when (locale.language) {
            "es" -> day?.abbreviationES?.first().toString()
            else -> day?.abbreviationEN?.first().toString()
        }
    }
}

fun checkSelectedDays(selectedDays: List<Int>): Pair<Boolean, Boolean> {
    val allDaysSelected = selectedDays.size == DayOfWeek.entries.size
    val weekdaysSelected = selectedDays.size == 5 && selectedDays.containsAll(listOf(0, 1, 2, 3, 4)) // Lunes a Viernes

    return Pair(allDaysSelected, weekdaysSelected)
}