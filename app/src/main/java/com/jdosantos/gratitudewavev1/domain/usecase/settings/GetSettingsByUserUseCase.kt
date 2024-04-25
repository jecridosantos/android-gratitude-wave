package com.jdosantos.gratitudewavev1.domain.usecase.settings

import com.jdosantos.gratitudewavev1.domain.models.UserSettings
import com.jdosantos.gratitudewavev1.domain.repository.ConfigUserRepository
import javax.inject.Inject

class GetSettingsByUserUseCase @Inject constructor(
    private val configUserRepository: ConfigUserRepository
) {
    fun execute(callback: (UserSettings) -> Unit, onError: () -> Unit) {
        configUserRepository.getByUser(callback, onError)
    }
}