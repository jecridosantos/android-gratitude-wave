package com.jdosantos.gratitudewavev1.domain.handles

import androidx.annotation.StringRes
import com.jdosantos.gratitudewavev1.R

sealed class ReminderRepetitions(
    val id: Int,
    @StringRes val title: Int
) {
    data object Once : ReminderRepetitions(0, R.string.label_repeat_once)
    data object Daily : ReminderRepetitions(1, R.string.label_repeat_daily)
    data object MonToFri : ReminderRepetitions(2, R.string.label_repeat_mon_to_fri)
    data object Custom : ReminderRepetitions(3, R.string.label_repeat_customized)
}