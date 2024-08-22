package com.jdosantos.gratitudewavev1.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceManager @Inject constructor(context: Context) {
    companion object {
        private const val PREFS_NAME = "my_preferences"
        private const val KEY_SHOW_ONBOARDING = "show_onboarding"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setShowOnboarding(show: Boolean) {
        preferences.edit().putBoolean(KEY_SHOW_ONBOARDING, show).apply()
    }

    fun shouldShowOnboarding(): Boolean {
        return preferences.getBoolean(KEY_SHOW_ONBOARDING, true)
    }

    fun clearAll() {
        preferences.edit().clear().apply()
    }
}