package com.jdosantos.gratitudewavev1.ui.view.main.profile.settings

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.data.local.RemindersStore
import com.jdosantos.gratitudewavev1.domain.models.UserSettingReminders
import com.jdosantos.gratitudewavev1.domain.models.UserSettings
import com.jdosantos.gratitudewavev1.domain.usecase.settings.GetSettingsByUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.settings.SaveSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSettingsByUserUseCase: GetSettingsByUserUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase
) : ViewModel() {
    private val tag = this::class.java.simpleName
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _userSettings = MutableStateFlow(UserSettings())
    val userSettings: StateFlow<UserSettings> = _userSettings

    private val _userSettingsFromNotificaiton = MutableStateFlow(UserSettings())

    var currentReminder by mutableStateOf(UserSettingReminders())
        private set

    init {
        getSettingsByUserUseCase.execute(callback = {
            _userSettingsFromNotificaiton.value = it
        }, onError = {
            Log.e(tag, "init - getSettingsByUserUseCase")
        })
    }

    fun getSettings() {
        getSettingsByUserUseCase.execute(callback = {
            _userSettings.value = it
        }, onError = {
            Log.e(tag, "getSettings - getSettingsByUserUseCase")
        })
    }

    private fun saveSettings(callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                saveSettingsUseCase.execute(_userSettings.value, callback)
            } catch (e: Exception) {
                Log.e(tag, "saveSettings - error ${e.localizedMessage}")
            }
        }

    }

    fun saveMuteNotifications(mute: Boolean, callback: (Boolean) -> Unit) {
        _userSettings.value = _userSettings.value!!.copy(muteNotifications = mute)
        saveSettings(callback);
    }


    fun fillReminderHour(hour: Int, minute: Int) {
        currentReminder = currentReminder.copy(hour = hour, minute = minute)
    }

    fun fillReminderLabel(label: String) {
        val labelClean = label.replace("|", " ").replace(",", " ").replace("_", " ")
        currentReminder = currentReminder.copy(label = labelClean)
    }

    fun fillReminderRepeat(repeatConfig: Int) {
        currentReminder = currentReminder.copy(repeat = repeatConfig)
    }

    fun fillReminderRepeatDays(repeatDays: MutableList<Int>) {
        currentReminder = currentReminder.copy(repeatDays = repeatDays)
    }

    fun addReminder(context: Context, callback: (Boolean) -> Unit) {
        currentReminder = currentReminder.copy(uuid = UUID.randomUUID().toString())
        _userSettings.value.reminders.add(currentReminder)
        _userSettings.value =
            _userSettings.value.copy(reminders = _userSettings.value.reminders.toMutableList())
        saveSettings(callback = {
            storeReminders(context)
            callback(it)
        });
        cleanReminder()
    }

    private fun storeReminders(context: Context) {
        val remindersSet: Set<String> = _userSettings.value!!.reminders.toSetOfString()

        val store = RemindersStore(context)
        viewModelScope.launch(Dispatchers.IO) {
            store.saveRemindersSet(remindersSet)
        }
    }

    fun List<UserSettingReminders>.toSetOfString(): Set<String> {
        return this.map { reminder ->
            reminder.toString()
        }.toSet()
    }

    fun cleanReminder() {
        currentReminder = currentReminder.copy(
            uuid = "",
            hour = 0,
            minute = 0,
            label = "",
            repeat = 0,
            active = true,
            repeatDays = mutableListOf()
        )
    }

    fun fillReminder(index: Int) {
        Log.d("TIMEPICKER 4.0", "index: $index")
        if (_userSettings.value.reminders.isNotEmpty()) {
            val reminderToEdit = _userSettings.value.reminders[index]

            Log.d("TIMEPICKER 4", "reminderToEdit: $reminderToEdit")

            currentReminder = currentReminder.copy(
                uuid = reminderToEdit.uuid,
                hour = reminderToEdit.hour,
                minute = reminderToEdit.minute,
                label = reminderToEdit.label,
                repeat = reminderToEdit.repeat,
                repeatDays = reminderToEdit.repeatDays,
            )
            Log.d("TIMEPICKER 4.1", "currentReminder: $currentReminder")
        }
    }

    fun disableReminder(index: Int) {
        if (_userSettingsFromNotificaiton.value.reminders.isNotEmpty()) {
            try {
                var reminderToEdit = _userSettingsFromNotificaiton.value.reminders[index]
                reminderToEdit = reminderToEdit.copy(active = false)

                _userSettingsFromNotificaiton.value.reminders[index] = reminderToEdit
                _userSettingsFromNotificaiton.value =
                    _userSettingsFromNotificaiton.value.copy(reminders = _userSettingsFromNotificaiton.value.reminders.toMutableList())
                Log.d(
                    tag,
                    "disableReminder - _userSettingsFromNotificaiton ${_userSettingsFromNotificaiton.value}"
                )
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        saveSettingsUseCase.execute(_userSettingsFromNotificaiton.value) {
                            Log.d(tag, "disableReminder -  saveSettingsUseCase.execute success $it")
                        }
                    } catch (e: Exception) {
                        Log.e(
                            tag,
                            "disableReminder -  saveSettingsUseCase.execute ${e.localizedMessage}"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    tag,
                    "disableReminder - error: ${e.localizedMessage}"
                )
            }

        }

    }

    fun updateReminder(
        context: Context,
        index: Int,
        callback: (Boolean) -> Unit
    ) {
        if (index in 0 until _userSettings.value.reminders.size) {
            _userSettings.value.reminders[index] = currentReminder
            _userSettings.value =
                _userSettings.value.copy(reminders = _userSettings.value.reminders.toMutableList())
        }

        saveSettings(callback = {
            storeReminders(context)
            callback(it)
        });
    }

    fun deleteReminder(
        context: Context,
        index: Int,
        callback: (Boolean) -> Unit
    ) {
        if (index in 0 until _userSettings.value.reminders.size) {
            _userSettings.value = _userSettings.value.copy(
                reminders = _userSettings.value.reminders.toMutableList().apply {
                    removeAt(index)
                }
            )
        }

        saveSettings(callback = {
            storeReminders(context)
            callback(it)
        })
    }

    fun updateReminderState(
        context: Context,
        index: Int,
        active: Boolean,
        callback: (Boolean) -> Unit
    ) {

        Log.d("REMINDER UPDATE", "index: $index, active: $active")

        if (index in 0 until _userSettings.value.reminders.size) {
            _userSettings.value.reminders[index] =
                _userSettings.value.reminders[index].copy(active = active)
            _userSettings.value =
                _userSettings.value.copy(reminders = _userSettings.value.reminders.toMutableList())
        }

        saveSettings(callback = {
            storeReminders(context)
            callback(it)
        });
    }
}