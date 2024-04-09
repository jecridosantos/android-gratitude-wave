package com.jdosantos.gratitudewavev1.app.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteTypeStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("NoteTypeConfig")
        private val DEFAULT_KEY = stringPreferencesKey("DEFAULT")

    }

    private suspend fun saveValue(index: String) = context.dataStore.edit { preferences ->
        preferences[DEFAULT_KEY] = index
    }

    fun getValue(): Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[DEFAULT_KEY] ?: "0"
        }

    suspend fun saveDefault(index: Int) {
        saveValue(index.toString())
    }


}