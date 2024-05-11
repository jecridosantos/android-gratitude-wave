package com.jdosantos.gratitudewavev1.domain.repository

import com.jdosantos.gratitudewavev1.domain.models.UserData

interface UserRepository {
    fun saveUser(userData: UserData, callback: (success: Boolean) -> Unit)

    fun getUserById(id: String, callback: (userData: UserData) -> Unit, onError: () -> Unit)

    fun getUserByUid(uid: String, callback: (userData: UserData?) -> Unit, onError: () -> Unit)
}