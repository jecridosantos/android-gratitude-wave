package com.jdosantos.gratitudewavev1.app.usecase

import com.jdosantos.gratitudewavev1.app.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    fun execute(onSuccess: () -> Unit, onError: () -> Unit) {
        authRepository.logout(onSuccess, onError)
    }
}