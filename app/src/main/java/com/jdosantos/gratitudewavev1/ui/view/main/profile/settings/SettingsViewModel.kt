package com.jdosantos.gratitudewavev1.ui.view.main.profile.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.models.UserSettings
import com.jdosantos.gratitudewavev1.domain.models.UserSettingReminders
import com.jdosantos.gratitudewavev1.domain.usecase.settings.GetSettingsByUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.settings.SaveSettingsUseCase
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsRouteParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSettingsByUserUseCase: GetSettingsByUserUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase
) : ViewModel() {
    val index: String = checkNotNull(savedStateHandle[ConstantsRouteParams.REMINDER_INDEX]?:"")
    private val tag = this::class.java.simpleName
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _UserSettings = MutableStateFlow(UserSettings())
    val userSettings: StateFlow<UserSettings> = _UserSettings

    var currentReminder by mutableStateOf(UserSettingReminders())
        private set

    fun getSettings() {
        getSettingsByUserUseCase.execute(callback = {
            _UserSettings.value = it
        }, onError = {
            Log.e(tag, "getSettings - getSettingsByUserUseCase")
        })
    }

    private fun saveSettings(callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                saveSettingsUseCase.execute(_UserSettings.value, callback)
            } catch (e: Exception) {
                Log.e(tag, "saveSettings - error ${e.localizedMessage}")
            }
        }

    }

    // Función para guardar el estado de notificaciones silenciadas
    fun saveMuteNotifications(mute: Boolean, callback: (Boolean) -> Unit) {
        _UserSettings.value = _UserSettings.value.copy(muteNotifications = mute)
        saveSettings(callback);
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

    fun addReminder(callback: (Boolean) -> Unit) {
        _UserSettings.value.reminders.add(currentReminder)
        _UserSettings.value =
            _UserSettings.value.copy(reminders = _UserSettings.value.reminders.toMutableList())
        saveSettings(callback);
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

        val reminderToEdit = _UserSettings.value.reminders[index]

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
        index: Int,
        callback: (Boolean) -> Unit
    ) {
        if (index in 0 until _UserSettings.value.reminders.size) {
            _UserSettings.value.reminders[index] = currentReminder
            _UserSettings.value =
                _UserSettings.value.copy(reminders = _UserSettings.value.reminders.toMutableList())
        }

        saveSettings(callback);
    }

    // Función para actualizar el estado de un elemento de la lista de recordatorios
    fun updateReminderState(index: Int, active: Boolean, callback: (Boolean) -> Unit) {

        Log.d("REMINDER UPDATE", "index: $index, active: $active")

        if (index in 0 until _UserSettings.value.reminders.size) {
            _UserSettings.value.reminders[index] =
                _UserSettings.value.reminders[index].copy(active = active)
            _UserSettings.value =
                _UserSettings.value.copy(reminders = _UserSettings.value.reminders.toMutableList())
        }

        saveSettings(callback);
    }
}