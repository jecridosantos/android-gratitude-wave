package com.jdosantos.gratitudewavev1.domain.repository

import com.jdosantos.gratitudewavev1.domain.models.User

interface UserRepository {
    fun saveUser(user: User, callback: (success: Boolean) -> Unit)

    fun getUserById(id: String, callback: (user: User) -> Unit, onError: () -> Unit)

    fun getUserByUid(uid: String, callback: (user: User?) -> Unit, onError: () -> Unit)
}