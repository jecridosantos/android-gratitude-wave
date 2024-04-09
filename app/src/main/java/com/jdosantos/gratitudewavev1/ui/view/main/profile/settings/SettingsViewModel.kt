package com.jdosantos.gratitudewavev1.ui.view.main.profile.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.app.model.ConfigUser
import com.jdosantos.gratitudewavev1.app.model.ConfigUserReminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /*  var configUser by mutableStateOf(ConfigUser())
          private set*/

    private val _configUser = MutableStateFlow(ConfigUser())
    val configUser: StateFlow<ConfigUser> = _configUser


    private val _reminders = MutableStateFlow<List<ConfigUserReminder>>(emptyList())
    val reminders: StateFlow<List<ConfigUserReminder>> = _reminders


    var currentReminder by mutableStateOf(ConfigUserReminder())
        private set

    // Función para guardar el estado de notificaciones silenciadas
    fun saveMuteNotifications(mute: Boolean) {
        _configUser.value = _configUser.value.copy(muteNotifications = mute)
    }

    // Función para agregar un recordatorio a la lista

    fun fillReminderHour(hour: Int, minute: Int) {
        currentReminder = currentReminder.copy(hour = hour, minute = minute)
    }

    fun fillReminderLabel(label: String) {
        currentReminder = currentReminder.copy(label = label)
    }


    fun fillReminderRepeat(repeatConfig: Int) {
        currentReminder = currentReminder.copy(repeat = repeatConfig)
    }

    fun fillReminderRepeatDays(repeatDays: MutableList<Int>) {
        currentReminder = currentReminder.copy(repeatDays = repeatDays)
    }


    fun addReminder() {
        _configUser.value.reminders.add(currentReminder)
        _configUser.value =
            _configUser.value.copy(reminders = _configUser.value.reminders.toMutableList())

        cleanReminder()
    }

    fun cleanReminder() {
        currentReminder = currentReminder.copy(
            hour = 0,
            minute = 0,
            label = "",
            repeat = 0,
            active = true,
            repeatDays = mutableListOf()
        )
    }

    fun fillReminder(index: Int) {

        val reminderToEdit = _configUser.value.reminders[index]

        Log.d("CURRENT REMINDER", "reminderToEdit: $reminderToEdit")

        currentReminder = currentReminder.copy(
            hour = reminderToEdit.hour,
            minute = reminderToEdit.minute,
            label = reminderToEdit.label,
            repeat = reminderToEdit.repeat,
            repeatDays = reminderToEdit.repeatDays,
        )
        Log.d("CURRENT REMINDER", "currentReminder: $currentReminder")
    }

    // Función para actualizar un elemento de la lista de recordatorios
    fun updateReminder(
        index: Int
    ) {
        if (index in 0 until _configUser.value.reminders.size) {
            _configUser.value.reminders[index] = currentReminder
            _configUser.value =
                _configUser.value.copy(reminders = _configUser.value.reminders.toMutableList())
        }
    }

    // Función para actualizar el estado de un elemento de la lista de recordatorios
    fun updateReminderState(index: Int, active: Boolean) {

        Log.d("REMINDER UPDATE", "index: $index, active: $active")

        if (index in 0 until _configUser.value.reminders.size) {
            _configUser.value.reminders[index] =
                _configUser.value.reminders[index].copy(active = active)
            _configUser.value =
                _configUser.value.copy(reminders = _configUser.value.reminders.toMutableList())
        }
    }
}