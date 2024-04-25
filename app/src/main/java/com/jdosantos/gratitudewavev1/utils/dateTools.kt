package com.jdosantos.gratitudewavev1.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.enums.TimeOfDay
import com.jdosantos.gratitudewavev1.domain.models.CalendarToShow
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@SuppressLint("SimpleDateFormat")
@Composable
fun getFormattedDate(date: Date): String {
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    val currentYear = today.get(Calendar.YEAR)
    val dateYear = Calendar.getInstance().apply { time = date }.get(Calendar.YEAR)

    val dayMonthFormat = if (dateYear == currentYear) SimpleDateFormat("d MMM, h:mm a") else SimpleDateFormat("d MMM, yyyy h:mm a")

    val timeFormat = SimpleDateFormat("h:mm a")
    return when {
        isToday(
            date,
            today
        ) -> timeFormat.format(date)

        isToday(
            date,
            yesterday
        ) -> "${stringResource(id = R.string.label_date_yesterday)} ${timeFormat.format(date)}"

        else -> dayMonthFormat.format(date)
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun getFormattedDateSimple(date: Date): String {
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    val currentYear = today.get(Calendar.YEAR)
    val dateYear = Calendar.getInstance().apply { time = date }.get(Calendar.YEAR)

    val dayMonthFormat = if (dateYear == currentYear) SimpleDateFormat("d MMM") else SimpleDateFormat("d MMM, yyyy")

    return when {
        isToday(
            date,
            today
        ) -> stringResource(id = R.string.label_today)

        isToday(
            date,
            yesterday
        ) -> stringResource(id = R.string.label_date_yesterday)

        else -> dayMonthFormat.format(date)
    }
}


fun isToday(date: Date, reference: Calendar): Boolean {
    val dateCalendar = Calendar.getInstance().apply { time = date }
    return dateCalendar.get(Calendar.YEAR) == reference.get(Calendar.YEAR) &&
            dateCalendar.get(Calendar.DAY_OF_YEAR) == reference.get(Calendar.DAY_OF_YEAR)
}

fun getCurrentDate(): String {
    val currentDate: Date = Calendar.getInstance().time
    val res = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return res.format(currentDate)
}

fun getFormattedDateToSearch(date: Date): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(date)
}

fun getTimeOfDay(): TimeOfDay {
    val time = LocalTime.now().hour
    return when (time) {
        in 0..11 -> TimeOfDay.GOOD_MORNING
        in 12..17 -> TimeOfDay.GOOD_AFTERNOON
        else -> TimeOfDay.GOOD_NIGHT
    }
}

fun getDayOfMonth(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun compareDatesWithoutTime(from: Date, to: Date): Boolean {

    val localDate1 = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val localDate2 = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

    return localDate2 == localDate1
}

fun convertMillisToDate(millis: Long): Date? {
    val timeZone = TimeZone.getDefault()
    return Date(millis - timeZone.rawOffset)
}


private fun initCalendar(date: Date): Calendar {
    val calendar = Calendar.getInstance().apply { time = date }
    calendar.set(Calendar.DAY_OF_MONTH,1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar
}

fun getMonthsBetweenDates(startDate: Date, endDate: Date): List<CalendarToShow> {
    val startCalendar = initCalendar(startDate)
    val endCalendar = initCalendar(endDate)

    val months = mutableListOf<CalendarToShow>()
    val yearDateFormat = SimpleDateFormat("YYYY", Locale.getDefault())
    val monthDateFormat = SimpleDateFormat("MM", Locale.getDefault())
    val monthNameFormat = SimpleDateFormat("MMM", Locale.getDefault())

    while (startCalendar.before(endCalendar) || startCalendar == endCalendar) {
        // Agrega el mes en formato numérico y nombre abreviado
        val yearNumber = yearDateFormat.format(startCalendar.time)
        val monthNumber = monthDateFormat.format(startCalendar.time)
        val monthName = monthNameFormat.format(startCalendar.time).toUpperCase()
        months.add(CalendarToShow(convertToInt(monthNumber), convertToInt(yearNumber), monthName, emptyList()))

        // Incrementa el mes
        startCalendar.add(Calendar.MONTH, 1)
    }

    return months
}

fun hourFormat(hour: Int, minutes: Int): String {
    val amPm = if (hour < 12) "a.m." else "p.m."
    val hora12 = if (hour % 12 == 0) 12 else hour % 12
    return String.format("%02d:%02d %s", hora12, minutes, amPm)
}
