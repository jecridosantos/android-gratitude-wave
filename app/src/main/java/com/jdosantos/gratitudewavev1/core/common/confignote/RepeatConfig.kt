package com.jdosantos.gratitudewavev1.core.common.confignote

import androidx.annotation.StringRes
import com.jdosantos.gratitudewavev1.R

sealed class RepeatConfig(
    val id: Int,
    @StringRes val title: Int
) {
    data object Once : RepeatConfig(0, R.string.label_repeat_once)
    data object Daily : RepeatConfig(1, R.string.label_repeat_daily)
    data object MonToFri : RepeatConfig(2, R.string.label_repeat_mon_to_fri)
    data object Custom : RepeatConfig(3, R.string.label_repeat_customized)
}