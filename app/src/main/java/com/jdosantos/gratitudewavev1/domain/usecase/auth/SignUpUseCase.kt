package com.jdosantos.gratitudewavev1.domain.usecase.auth

import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(user: User, password: String, callback: (success: Boolean) -> Unit) {
        authRepository.signUp(
            user.email,
            password,
            callback = { success ->
                if (success) {
                    userRepository.saveUser(user, callback)
                } else {
                    callback.invoke(false)
                }
            }
        )
    }

    fun sendEmailVerification(callback: (success: Boolean) -> Unit) {
        authRepository.sendEmailVerification(callback)
    }
}