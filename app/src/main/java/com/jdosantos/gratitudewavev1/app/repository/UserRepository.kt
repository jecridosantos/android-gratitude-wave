package com.jdosantos.gratitudewavev1.app.repository

import com.jdosantos.gratitudewavev1.app.model.User

interface UserRepository {
    fun saveUser(user: User, onSuccess: () -> Unit, onError: (error: String) -> Unit)

    fun getUserById(id: String, onSuccess: (user: User) -> Unit, onError: (error: String) -> Unit)

    fun getUserByUid(uid: String, onSuccess: (user: User?) -> Unit, onError: (error: String) -> Unit)
}