package com.jdosantos.gratitudewavev1.domain.models

data class CalendarToShow(
    val month: Int,
    val year: Int,
    val display: String,
    var days: List<DaysOfCalendar?>
)