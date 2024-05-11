package com.jdosantos.gratitudewavev1.domain.usecase.auth

import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun execute(): Result<User> {
        return authRepository.getCurrentUser()
    }
}