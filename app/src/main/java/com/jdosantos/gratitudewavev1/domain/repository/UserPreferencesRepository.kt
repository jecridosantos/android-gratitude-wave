package com.jdosantos.gratitudewavev1.domain.repository

import com.jdosantos.gratitudewavev1.domain.models.UserPreferences

interface UserPreferencesRepository {
    suspend fun saveUserPreferences(userPreferences: UserPreferences) : Result<UserPreferences>

    suspend fun updateUserPreferences(userPreferences: UserPreferences) : Result<UserPreferences>
    suspend fun getUserPreferences(): Result<UserPreferences>
}