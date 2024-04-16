package com.jdosantos.gratitudewavev1.core.common.util

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.jdosantos.gratitudewavev1.app.enums.DayOfWeek
import com.jdosantos.gratitudewavev1.core.common.confignote.Challenge
import com.jdosantos.gratitudewavev1.core.common.confignote.RepeatConfig
import com.jdosantos.gratitudewavev1.core.common.constants.Constants
import java.util.Locale

fun convertToInt(value: String?): Int {
    return try {
        if (value!!.isEmpty()) {
            -1
        } else {
            value.toInt()
        }
    } catch (e: Exception) {
        Log.d("Error", e.toString())
        -1
    }

}

fun List<Color>.getSafeColor(index: Int?): Color {
    val safeIndex = index ?: Constants.DEFAULT_COLOR_INDEX
    return getOrElse(safeIndex) { this[Constants.DEFAULT_COLOR_INDEX] }
}

fun getWeekDaysByLocale(fullNames: Boolean = true): List<String> {
    val locale = Locale.getDefault()
    val daysInSpanish = enumValues<DayOfWeek>().map { if (fullNames) it.nameES else it.abbreviationES }
    val daysInEnglish = enumValues<DayOfWeek>().map { if (fullNames) it.nameEN else it.abbreviationEN }

    return if (locale.language == Locale("es", "ES").language) {
        daysInSpanish
    } else {
        daysInEnglish
    }
}

val repeatListOptions = listOf(
    RepeatConfig.Once,
    RepeatConfig.Daily,
    RepeatConfig.MonToFri,
    RepeatConfig.Custom
)