package com.jdosantos.gratitudewavev1.domain.usecase.userPreferences

import com.jdosantos.gratitudewavev1.data.local.UserPreferencesManager
import com.jdosantos.gratitudewavev1.domain.exceptions.GenericException
import com.jdosantos.gratitudewavev1.domain.models.UserPreferences
import com.jdosantos.gratitudewavev1.domain.repository.UserPreferencesRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userPreferencesManager: UserPreferencesManager,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userPreferences: UserPreferences): Result<UserPreferences> {
        return try {
            if (userPreferences.id.isEmpty()) {
                userPreferencesRepository.saveUserPreferences(userPreferences)
            } else {
                userPreferencesRepository.updateUserPreferences(userPreferences)
            }.mapCatching {

                if (userPreferences.name.isNotEmpty()) {
                    userRepository.updateName(userId = userPreferences.userId, name = userPreferences.name)
                }

                userPreferencesManager.saveUserPreferences(userPreferences)
                it
            }
        } catch (e: Exception) {
            Result.failure(GenericException.ClientException())
        }

    }
}