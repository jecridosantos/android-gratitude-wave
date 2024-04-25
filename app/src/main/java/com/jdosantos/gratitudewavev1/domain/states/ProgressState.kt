package com.jdosantos.gratitudewavev1.domain.states

data class ProgressState(
    val totalNotes: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0
)
