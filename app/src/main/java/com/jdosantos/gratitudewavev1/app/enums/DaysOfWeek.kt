package com.jdosantos.gratitudewavev1.app.enums

import com.jdosantos.gratitudewavev1.core.common.constants.Constants.Companion.WORK_WEEK_SIZE
import java.util.Locale

enum class DayOfWeek(
    val index: Int,
    val nameES: String,
    val nameEN: String,
    val abbreviationES: String,
    val abbreviationEN: String
) {
    MON(0, "Lunes", "Monday", "Lun", "Mon"),
    TUE(1, "Martes", "Tuesday", "Mar", "Tue"),
    WED(2, "Miércoles", "Wednesday", "Mié", "Wed"),
    THU(3, "Jueves", "Thursday", "Jue", "Thu"),
    FRI(4, "Viermes", "Friday", "Vie", "Fri"),
    SAT(5, "Sábado", "Saturday", "Sáb", "Sat"),
    SUN(6, "Domingo", "Sunday", "Dom", "Sun")
}

fun getFirstLetters(selectedDays: List<Int>, locale: Locale = Locale.getDefault()): String {
    val daysOfWeek = DayOfWeek.entries.toTypedArray()
    val dayMap = daysOfWeek.associateBy { it.index }

    return selectedDays.sorted().joinToString(", ") { index ->
        val day = dayMap[index]
        when (locale.language) {
            "es" -> day?.abbreviationES ?: ""
            else -> day?.abbreviationEN ?: ""
        }
    }
}

fun checkSelectedDays(selectedDays: List<Int>): Pair<Boolean, Boolean> {
    val allDaysSelected = selectedDays.size == DayOfWeek.entries.size
    val weekdaysSelected = selectedDays.size == WORK_WEEK_SIZE && selectedDays.containsAll(
        listOf(
            DayOfWeek.MON.index,
            DayOfWeek.TUE.index,
            DayOfWeek.WED.index,
            DayOfWeek.THU.index,
            DayOfWeek.FRI.index
        )
    ) // Lunes a Viernes

    return Pair(allDaysSelected, weekdaysSelected)
}