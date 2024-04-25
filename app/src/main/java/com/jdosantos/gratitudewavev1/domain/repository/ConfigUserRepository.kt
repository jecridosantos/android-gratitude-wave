package com.jdosantos.gratitudewavev1.domain.repository

import com.jdosantos.gratitudewavev1.domain.models.UserSettings

interface ConfigUserRepository {
    fun save(body: UserSettings, callback: (success: Boolean) -> Unit)

    fun getByUser(callback: (UserSettings) -> Unit, onError: ()-> Unit)

    fun update(body: UserSettings, callback: (success: Boolean) -> Unit)
}