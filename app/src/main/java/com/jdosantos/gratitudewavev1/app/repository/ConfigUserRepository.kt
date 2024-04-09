package com.jdosantos.gratitudewavev1.app.repository

import com.jdosantos.gratitudewavev1.app.model.ConfigUser

interface ConfigUserRepository {
    fun save(body: ConfigUser, onSuccess: () -> Unit, onError: (error: String) -> Unit)

    fun getByUser(callback: (ConfigUser) -> Unit)

    fun update(body: ConfigUser, onSuccess: () -> Unit, onError: (error: String) -> Unit)
}