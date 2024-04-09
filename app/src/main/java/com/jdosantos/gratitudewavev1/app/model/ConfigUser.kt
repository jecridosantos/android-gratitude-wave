package com.jdosantos.gratitudewavev1.app.model

data class ConfigUser(
    val id: String? = "",
    val uid: String? = "",
    val muteNotifications: Boolean = false,
    val reminders: MutableList<ConfigUserReminder> = mutableListOf()
)

data class ConfigUserReminder(
    val hour: Int? = 0,
    val minute: Int? = 0,
    val label: String? = "",
    val repeat: Int = 0,
    val active: Boolean = true,
    val repeatDays: MutableList<Int>? = mutableListOf()
)