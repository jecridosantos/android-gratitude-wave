package com.jdosantos.gratitudewavev1.app.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CredentialStore(private val context: Context) {


    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Credential")
        private val PASSWORD_KEY = stringPreferencesKey("PASSWORD")

    }

    suspend fun saveValue(password: String) = context.dataStore.edit { preferences ->
        preferences[PASSWORD_KEY] = password
    }

    fun getValue(): Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PASSWORD_KEY] ?: "0"
        }


}