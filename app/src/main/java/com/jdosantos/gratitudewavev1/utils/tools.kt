package com.jdosantos.gratitudewavev1.utils

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.jdosantos.gratitudewavev1.utils.constants.Constants

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

