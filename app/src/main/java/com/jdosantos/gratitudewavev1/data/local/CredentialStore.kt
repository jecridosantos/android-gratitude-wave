package com.jdosantos.gratitudewavev1.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CredentialStore  @Inject constructor(val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Credential")
        private val PASSWORD_KEY = stringPreferencesKey("PASSWORD")

    }

    suspend fun savePassword(password: String) = context.dataStore.edit { preferences ->
        preferences[PASSWORD_KEY] = password
    }

    fun getPassword(): Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PASSWORD_KEY] ?: "0"
        }

    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}