package com.jdosantos.gratitudewavev1.domain.usecase.settings

import com.jdosantos.gratitudewavev1.domain.models.UserSettings
import com.jdosantos.gratitudewavev1.domain.repository.ConfigUserRepository
import javax.inject.Inject

class SaveSettingsUseCase @Inject constructor(
    private val configUserRepository: ConfigUserRepository
) {
    fun execute(settings: UserSettings, callback: (success: Boolean) -> Unit) {
        if (settings.id.isNullOrEmpty()) {
            configUserRepository.save(settings, callback)
        } else {
            configUserRepository.update(settings, callback)
        }

    }
}