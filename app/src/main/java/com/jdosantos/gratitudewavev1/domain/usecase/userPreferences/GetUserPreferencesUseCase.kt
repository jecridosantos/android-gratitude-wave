package com.jdosantos.gratitudewavev1.domain.usecase.userPreferences

import com.jdosantos.gratitudewavev1.domain.models.UserPreferences
import com.jdosantos.gratitudewavev1.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class GetUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Result<UserPreferences> =
        userPreferencesRepository.getUserPreferences()

}