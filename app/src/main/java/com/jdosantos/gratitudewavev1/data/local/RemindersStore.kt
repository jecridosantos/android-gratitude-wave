package com.jdosantos.gratitudewavev1.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class RemindersStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Reminders")
        private val REMINDERS_KEY = stringSetPreferencesKey("REMINDERS")
    }

    suspend fun saveReminder(reminders: String) {
        val currentSet = getReminders().firstOrNull() ?: emptySet()
        val newSet = currentSet.toMutableSet().apply { add(reminders) }
        context.dataStore.edit { preferences ->
            preferences[REMINDERS_KEY] = newSet
        }
    }

    suspend fun saveRemindersSet(remindersSet: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences.remove(REMINDERS_KEY)
            preferences[REMINDERS_KEY] = remindersSet
        }
    }

    fun getReminders(): Flow<Set<String>?> = context.dataStore.data
        .map { preferences ->
            preferences[REMINDERS_KEY] ?: emptySet()
        }
}