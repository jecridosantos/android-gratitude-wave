package com.jdosantos.gratitudewavev1.domain.usecase.auth

import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import javax.inject.Inject

class ReauthenticateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun execute(password: String): Result<User> {
        return authRepository.reauthenticate(password)
    }

}