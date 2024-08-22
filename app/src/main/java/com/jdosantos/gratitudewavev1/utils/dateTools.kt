package com.jdosantos.gratitudewavev1.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.enums.TimeOfDay
import com.jdosantos.gratitudewavev1.domain.models.CalendarToShow
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.isSameDay(reference: Calendar): Boolean {
    val dateCalendar = this.toCalendar()
    return dateCalendar.get(Calendar.YEAR) == reference.get(Calendar.YEAR) &&
            dateCalendar.get(Calendar.DAY_OF_YEAR) == reference.get(Calendar.DAY_OF_YEAR)
}

fun Date.toCalendar(): Calendar = Calendar.getInstance().apply { time = this@toCalendar }

@SuppressLint("SimpleDateFormat")
@Composable
fun getFormattedDate(date: Date): String {
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    val currentYear = today.get(Calendar.YEAR)
    val dateYear = date.toCalendar().get(Calendar.YEAR)

    val dayMonthFormat =
        SimpleDateFormat(if (dateYear == currentYear) "d MMM, h:mm a" else "d MMM, yyyy h:mm a")
    val timeFormat = SimpleDateFormat("h:mm a")

    return when {
        date.isSameDay(today) -> timeFormat.format(date)
        date.isSameDay(yesterday) -> "${stringResource(R.string.label_date_yesterday)} ${
            timeFormat.format(
                date
            )
        }"

        else -> dayMonthFormat.format(date)
    }
}


@SuppressLint("SimpleDateFormat")
@Composable
fun getFormattedDateSimple(date: Date): String {
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }

    val currentYear = today.get(Calendar.YEAR)
    val dateYear = date.toCalendar().get(Calendar.YEAR)

    val dayMonthFormat = SimpleDateFormat(if (dateYear == currentYear) "d MMM" else "d MMM, yyyy")

    return when {
        date.isSameDay(today) -> stringResource(R.string.label_today)
        date.isSameDay(yesterday) -> stringResource(R.string.label_date_yesterday)
        else -> dayMonthFormat.format(date)
    }
}

fun getCurrentDate(): String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

fun getCurrentDate(date: Date): String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)

fun getFormattedDateToSearch(date: Date): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)

@SuppressLint("SimpleDateFormat")
fun getDateFromString(dateString: String): Date {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return dateFormat.parse(dateString)!!
}

fun getTimeOfDay(): TimeOfDay {
    val time = LocalTime.now().hour
    return when (time) {
        in 0..11 -> TimeOfDay.GOOD_MORNING
        in 12..17 -> TimeOfDay.GOOD_AFTERNOON
        else -> TimeOfDay.GOOD_NIGHT
    }
}

fun combineDateAndTime(date: Date, time: Date): Date {
    val calendarDate = Calendar.getInstance()
    calendarDate.time = date

    val calendarTime = Calendar.getInstance()
    calendarTime.time = time

    // Comparar si el día, mes y año de ambas fechas son iguales
    if (calendarDate.get(Calendar.YEAR) == calendarTime.get(Calendar.YEAR) &&
        calendarDate.get(Calendar.MONTH) == calendarTime.get(Calendar.MONTH) &&
        calendarDate.get(Calendar.DAY_OF_MONTH) == calendarTime.get(Calendar.DAY_OF_MONTH)) {
        return time
    }

    // Combinar el día, mes y año de `date` con la hora, minutos y segundos de `time`
    calendarDate.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY))
    calendarDate.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE))
    calendarDate.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND))
    calendarDate.set(Calendar.MILLISECOND, calendarTime.get(Calendar.MILLISECOND))

    return calendarDate.time
}
fun getToday(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar
}

fun getDayOfMonth(date: Date): Int = date.toCalendar().get(Calendar.DAY_OF_MONTH)

fun compareDatesWithoutTime(from: Date, to: Date): Boolean {

    val localDate1 = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val localDate2 = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

    return localDate2 == localDate1
}

fun convertMillisToLocalDate(millis: Long) : LocalDate {
    return Instant
        .ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun convertMillisToDate(millis: Long): Date = Date(millis - TimeZone.getDefault().rawOffset)

private fun initCalendar(date: Date): Calendar = date.toCalendar().apply {
    set(Calendar.DAY_OF_MONTH, 1)
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun getMonthsBetweenDates(startDate: Date, endDate: Date): List<CalendarToShow> {
    val startCalendar = initCalendar(startDate)
    val endCalendar = initCalendar(endDate)

    val months = mutableListOf<CalendarToShow>()
    val yearDateFormat = SimpleDateFormat("YYYY", Locale.getDefault())
    val monthDateFormat = SimpleDateFormat("MM", Locale.getDefault())
    val monthNameFormat = SimpleDateFormat("MMM", Locale.getDefault())

    while (startCalendar <= endCalendar) {
        val yearNumber = yearDateFormat.format(startCalendar.time).toInt()
        val monthNumber = monthDateFormat.format(startCalendar.time).toInt()
        val monthName = monthNameFormat.format(startCalendar.time).uppercase(Locale.getDefault())

        months.add(CalendarToShow(monthNumber, yearNumber, monthName, emptyList()))
        startCalendar.add(Calendar.MONTH, 1)
    }

    return months
}

@SuppressLint("DefaultLocale")
fun hourFormat(hour: Int, minutes: Int): String {
    val amPm = if (hour < 12) "a.m." else "p.m."
    val hora12 = if (hour % 12 == 0) 12 else hour % 12
    return String.format("%02d:%02d %s", hora12, minutes, amPm)
}

fun convertMillisToDateTime(millis: Long): String {
    val date = Date(millis)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(date)
}

fun LocalDate.toDate(): Date = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())

fun Date.toLocalDate(): LocalDate = this.toInstant()
    .atZone(ZoneId.systemDefault())
    .toLocalDate()

fun uniqueDates(dates: List<Date>): List<Date> {
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