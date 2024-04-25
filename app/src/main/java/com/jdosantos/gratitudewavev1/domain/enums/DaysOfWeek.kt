package com.jdosantos.gratitudewavev1.domain.enums

import androidx.annotation.StringRes
import com.jdosantos.gratitudewavev1.R

enum class DayOfWeek(
    @StringRes val nameRes: Int
) {
    MON(R.string.monday),
    TUE(R.string.tuesday),
    WED(R.string.wednesday),
    THU(R.string.thursday),
    FRI(R.string.friday),
    SAT(R.string.saturday),
    SUN(R.string.sunday)
}

