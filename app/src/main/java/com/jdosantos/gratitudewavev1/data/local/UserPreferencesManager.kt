package com.jdosantos.gratitudewavev1.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.jdosantos.gratitudewavev1.domain.models.UserPreferences
import javax.inject.Inject

class UserPreferencesManager @Inject constructor(context: Context) {
    companion object {
        private const val PREFS_NAME = "UserPreferences"
        private const val PREF_USER_PREFERENCES = "user_preferences"
    }

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private val gson: Gson by lazy {
        Gson()
    }

    fun saveUserPreferences(userPreferences: UserPreferences) {
        val json = gson.toJson(userPreferences)
        preferences.edit().putString(PREF_USER_PREFERENCES, json).apply()
    }

    fun getUserPreferences(): UserPreferences {
        val json = preferences.getString(PREF_USER_PREFERENCES, null)
        return if (json != null) {
            gson.fromJson(json, UserPreferences::class.java)
        } else {
            UserPreferences()
        }
    }

    fun clearAll() {
        preferences.edit().clear().apply()
    }
}