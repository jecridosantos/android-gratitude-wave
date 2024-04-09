package com.jdosantos.gratitudewavev1.core.common.confignote

import com.jdosantos.gratitudewavev1.app.enums.TypeChallenge

sealed class Challenge(
    val id: String,
    val type: TypeChallenge,
    val title: String,
    val subtite: String,
    val minEntry: Int
) {

    data object DailyOne : Challenge(
        "DailyOne",
        TypeChallenge.DAILY,
        "Diario",
        "Puedes agradecer una vez al día",
        1
    )

    data object DailyTwo : Challenge(
        "DailyTwo",
        TypeChallenge.DAILY,
        "Diario x2",
        "Puedes agradecer 2 veces al día",
        2
    )

    data object WeeklyOne : Challenge(
        "WeeklyOne",
        TypeChallenge.WEEKLY,
        "Semanal",
        "Puedes agradecer una vez al día",
        7
    )

    data object WeeklyTwo : Challenge(
        "WeeklyTwo",
        TypeChallenge.WEEKLY,
        "Semanal x2",
        "Puedes agradecer 2 veces al día",
        14
    )
}