package com.jdosantos.gratitudewavev1.ui.view.main.home.bycalendar

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jdosantos.gratitudewavev1.domain.models.CalendarToShow
import com.jdosantos.gratitudewavev1.domain.models.DaysOfCalendar
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MID
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.SPACE_DEFAULT_MIN
import com.jdosantos.gratitudewavev1.utils.compareDatesWithoutTime
import com.jdosantos.gratitudewavev1.utils.getDayOfMonth
import com.jdosantos.gratitudewavev1.utils.getWeekDays
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CalendarView(
    monthsWithNotes: List<CalendarToShow>,
    selectedDate: Date,
    onClick: (Date) -> Unit
) {

    val pagerState = rememberPagerState(
        pageCount = monthsWithNotes.size,
       //      initialOffscreenLimit = monthsWithNotes.size - 1,
        infiniteLoop = false,
        initialPage = monthsWithNotes.size - 1
    )

    val currentPage = remember { pagerState.currentPage }

    val daysOfWeek = getWeekDays(LocalContext.current.resources, false)

    Card {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val currentMonth = monthsWithNotes[pagerState.currentPage].display
            Text(
                text = currentMonth.capitalize(Locale.ROOT),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SPACE_DEFAULT.dp)
            )
            Row(
                modifier = Modifier.padding(start = SPACE_DEFAULT.dp, end = SPACE_DEFAULT.dp)
            ) {
                daysOfWeek.forEach { day ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        DayWeekView(day)
                    }
                }
            }

            HorizontalPager(
                state = pagerState, modifier = Modifier
            ) { page ->
                MonthView(monthsWithNotes[page], selectedDate) { onClick(it) }
            }
        }
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun MonthView(
    monthsWithNotes: CalendarToShow,
    selectedDate: Date,
    onClick: (Date) -> Unit
) {

    val days = monthsWithNotes.days
    Column(
        modifier = Modifier
            .padding(start = SPACE_DEFAULT_MID.dp, end = SPACE_DEFAULT_MID.dp, bottom = SPACE_DEFAULT_MID.dp)
            .fillMaxWidth()
    ) {
        val weeksInMonth = days.chunked(7)
        weeksInMonth.forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                week.forEach { day ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        DayView(day!!, selectedDate) {
                            onClick(it)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun DayView(daysOfCalendar: DaysOfCalendar, selectedDate: Date, onClick: (Date) -> Unit) {
    val colorText = MaterialTheme.colorScheme.onBackground
    var colorBack =
        if (daysOfCalendar.isCurrentMonth) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant

    val isToday = compareDatesWithoutTime(Date(), daysOfCalendar.day)
    val isSelectedDay = compareDatesWithoutTime(selectedDate, daysOfCalendar.day)
    if (isToday) {
        colorBack = MaterialTheme.colorScheme.onPrimary
    }
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(colorBack, RoundedCornerShape(8.dp))
            .border(
                width = 2.dp,
                color = if (isSelectedDay) MaterialTheme.colorScheme.outline else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                if (daysOfCalendar.isCurrentMonth && !daysOfCalendar.isAfter) {
                    onClick(daysOfCalendar.day)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .padding(SPACE_DEFAULT_MIN.dp)
                .align(Alignment.Center),
            contentAlignment = Alignment.Center
        ) {


            // Texto del día
            if (daysOfCalendar.isCurrentMonth) {
                if (!daysOfCalendar.isAfter) {
                    Text(
                        text = "${getDayOfMonth(daysOfCalendar.day)}",
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "${getDayOfMonth(daysOfCalendar.day)}",
                        color = colorText,
                        fontWeight = FontWeight.Thin
                    )
                }

            }

            // Indicador de nota
            if (daysOfCalendar.hasNotes) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                )
            }
        }
    }

}

@Composable
private fun DayWeekView(day: String) {
    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic,
            fontSize = 12.sp
        )
    }
}
