package com.jdosantos.gratitudewavev1.domain.usecase.auth

import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import javax.inject.Inject

class SendEmailVerificationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun execute(): Result<Boolean> {
        return authRepository.sendEmailVerification()
    }
}