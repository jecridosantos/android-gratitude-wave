package com.jdosantos.gratitudewavev1.app.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FlagStore(private val context: Context) {


    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("FlagStore")
        private val SHOW_MESSAGE_FIRST_NOTE = intPreferencesKey("SHOW_MESSAGE_FIRST_NOTE")

    }

    suspend fun saveFirstNote(show: Int) = context.dataStore.edit { preferences ->
        preferences[SHOW_MESSAGE_FIRST_NOTE] = show
    }

    fun getFirstNote(): Flow<Int?> = context.dataStore.data
        .map { preferences ->
            preferences[SHOW_MESSAGE_FIRST_NOTE] ?: 1
        }


}