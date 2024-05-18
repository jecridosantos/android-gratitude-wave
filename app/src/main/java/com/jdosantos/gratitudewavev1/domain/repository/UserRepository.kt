package com.jdosantos.gratitudewavev1.domain.repository

import com.jdosantos.gratitudewavev1.domain.models.User

interface UserRepository {

    suspend fun saveUser(user: User): Result<User>

    suspend fun getUserByUid(uid: String): Result<User>

    suspend fun emailExists(email: String): Result<Boolean>

}