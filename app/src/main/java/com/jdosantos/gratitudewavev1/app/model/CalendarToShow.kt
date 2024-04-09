package com.jdosantos.gratitudewavev1.app.model

data class CalendarToShow(
    val month: Int,
    val year: Int,
    val display: String,
    var days: List<DaysOfCalendar?>
)