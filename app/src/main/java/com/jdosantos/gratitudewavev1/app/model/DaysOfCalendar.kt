package com.jdosantos.gratitudewavev1.app.model

import java.util.Date

data class DaysOfCalendar(
    val isCurrentMonth: Boolean = false,
    val isAfter: Boolean = false,
    var hasNotes: Boolean = false,
    val day: Date
)

