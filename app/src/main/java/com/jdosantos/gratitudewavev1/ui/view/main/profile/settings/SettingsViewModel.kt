package com.jdosantos.gratitudewavev1.ui.view.main.profile.settings

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    private val getSettingsByUserUseCase: GetSettingsByUserUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase
) : ViewModel() {

    private val tag = this::class.java.simpleName

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _userSettings = MutableStateFlow(UserSettings())
    val userSettings: StateFlow<UserSettings> = _userSettings

    var currentReminder by mutableStateOf(UserSettingReminders())
        private set

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
                Log.e(tag, "Failed to save settings: ${e.localizedMessage}")
            }
        }

    }

    private fun updateSettings(update: (UserSettings) -> UserSettings) {
        _userSettings.value = update(_userSettings.value)
    }


    fun saveMuteNotifications(mute: Boolean, callback: (Boolean) -> Unit) {
        updateSettings { it.copy(muteNotifications = mute) }
        saveSettings(callback);
    }


    fun fillReminderHour(hour: Int, minute: Int) {
        currentReminder = currentReminder.copy(hour = hour, minute = minute)
    }

    fun fillReminderLabel(label: String) {
        currentReminder = currentReminder.copy(label = label.clean())
    }

    private fun String.clean(): String {
        return replace("|", " ").replace(",", " ").replace("_", " ")
    }

    fun fillReminderRepeat(repeatConfig: Int) {
        currentReminder = currentReminder.copy(repeat = repeatConfig)
    }

    fun fillReminderRepeatDays(repeatDays: MutableList<Int>) {
        currentReminder = currentReminder.copy(repeatDays = repeatDays)
    }

    fun addReminder(context: Context, callback: (Boolean) -> Unit) {
        currentReminder = currentReminder.copy(uuid = UUID.randomUUID().toString())
        updateSettings { it.copy(reminders = (it.reminders + currentReminder).toMutableList()) }
        saveSettings {
            if (it) storeReminders(context)
            callback(it)
        }
        cleanReminder()
    }

    private fun storeReminders(context: Context) {
        val remindersSet: Set<String> = _userSettings.value.reminders.toSetOfString()

        val store = RemindersStore(context)
        viewModelScope.launch(Dispatchers.IO) {
            store.saveRemindersSet(remindersSet)
        }
    }

    private fun List<UserSettingReminders>.toSetOfString(): Set<String> {
        return map { it.toString() }.toSet()
    }

    fun cleanReminder() {
        currentReminder = UserSettingReminders()
    }

    fun fillReminder(index: Int) {
        if (index in _userSettings.value.reminders.indices) {
            currentReminder = _userSettings.value.reminders[index]
        }
    }

    fun updateReminder(context: Context, index: Int, callback: (Boolean) -> Unit) {
        if (index in _userSettings.value.reminders.indices) {
            _userSettings.value.reminders[index] = currentReminder
            saveSettings {
                if (it) storeReminders(context)
                callback(it)
            }
        }
    }

    fun deleteReminder(context: Context, index: Int, callback: (Boolean) -> Unit) {
        if (index in _userSettings.value.reminders.indices) {
            updateSettings {
                it.copy(reminders = it.reminders.toMutableList().apply { removeAt(index) })
            }
            saveSettings {
                if (it) storeReminders(context)
                callback(it)
            }
        }
    }

    fun updateReminderState(
        context: Context,
        index: Int,
        active: Boolean,
        callback: (Boolean) -> Unit
    ) {
        if (index in _userSettings.value.reminders.indices) {
            _userSettings.value.reminders[index] =
                _userSettings.value.reminders[index].copy(active = active)
            saveSettings {
                if (it) storeReminders(context)
                callback(it)
            }
        }
    }
}