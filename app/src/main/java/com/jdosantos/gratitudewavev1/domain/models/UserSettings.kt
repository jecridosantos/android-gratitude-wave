package com.jdosantos.gratitudewavev1.domain.models

data class UserSettings(
    val id: String? = "",
    val uid: String? = "",
    val muteNotifications: Boolean = false,
    val reminders: MutableList<UserSettingReminders> = mutableListOf()
)

data class UserSettingReminders(
    val hour: Int? = 0,
    val minute: Int? = 0,
    val label: String? = "",
    val repeat: Int = 0,
    val active: Boolean = true,
    val repeatDays: MutableList<Int>? = mutableListOf()
){
    override fun toString(): String {
        return "$hour,$minute|$label|$repeat|$active|${repeatDays?.joinToString(",")}"
    }

}