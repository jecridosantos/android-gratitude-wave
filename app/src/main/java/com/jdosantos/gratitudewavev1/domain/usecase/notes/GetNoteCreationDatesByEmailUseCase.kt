package com.jdosantos.gratitudewavev1.domain.usecase.notes

import com.jdosantos.gratitudewavev1.domain.models.CalendarToShow
import com.jdosantos.gratitudewavev1.domain.models.DaysOfCalendar
import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import com.jdosantos.gratitudewavev1.utils.compareDatesWithoutTime
import com.jdosantos.gratitudewavev1.utils.getMonthsBetweenDates
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class GetNoteCreationDatesByEmailUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {

    fun execute(callback: (List<Date>) -> Unit, onError: () -> Unit) {
        noteRepository.getNoteCreationDatesByEmail(callback, onError)

    }

    fun generateCalendar(callback: (List<CalendarToShow>) -> Unit, onError: () -> Unit) {
        execute(callback = {
            val months = generateMonths(it)
            callback.invoke(months)
        }, onError)

    }

    private fun generateMonths(dates: List<Date>): List<CalendarToShow> {

        val startDate = dates[0]
        val endDate = Date()
        val months = getMonthsBetweenDates(startDate, endDate)

        months.forEach { month ->
            val days = generateDaysInMonth(month.month, month.year)

            days.forEach { day ->
                val hasNote = dates.any { compareDatesWithoutTime(it, day!!.day) }
                day!!.hasNotes = hasNote
            }

            month.days = days
        }

        return months

    }

    private fun generateDaysInMonth(month: Int, year: Int): List<DaysOfCalendar?> {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        val days = mutableListOf<DaysOfCalendar>()


        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        if (firstDayOfWeek != Calendar.MONDAY) {
            val calendarPrev = Calendar.getInstance()
            calendarPrev.set(Calendar.YEAR, year)
            calendarPrev.set(Calendar.MONTH, month - 1)

            calendarPrev.add(Calendar.MONTH, -1)

            calendarPrev.set(
                Calendar.DAY_OF_MONTH,
                calendarPrev.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
            val daysInPreviousMonth = calendarPrev.get(Calendar.DAY_OF_WEEK)

            for (i in Calendar.MONDAY..daysInPreviousMonth) {
                calendarPrev.set(Calendar.DAY_OF_WEEK, i)
                days.add(DaysOfCalendar(false, false, false, calendarPrev.time))
            }

        }

        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val today = Calendar.getInstance()
        for (dayOfMonth in 1..daysInMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val dateToCheck = calendar.time
            days.add(DaysOfCalendar(true, dateToCheck.after(today.time), false, calendar.time))
        }

        val daysDifference = 35 - days.size
        while (days.size < 35) {

            val calendarNext = Calendar.getInstance()
            calendarNext.set(Calendar.YEAR, year)
            calendarNext.set(Calendar.MONTH, month - 1)

            calendarNext.add(Calendar.MONTH, 1)
            for (i in 1..daysDifference) {
                calendarNext.set(Calendar.DAY_OF_MONTH, i)
                days.add(DaysOfCalendar(false, true, false, calendarNext.time))
            }
        }
        return days
    }


}