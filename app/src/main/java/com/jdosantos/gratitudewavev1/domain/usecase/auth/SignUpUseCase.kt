package com.jdosantos.gratitudewavev1.domain.usecase.auth

import com.jdosantos.gratitudewavev1.domain.models.UserData
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(userData: UserData, password: String, callback: (success: Boolean) -> Unit) {
        authRepository.createUserWithEmailAndPassword(
            userData.email,
            password,
            callback = { success ->
                callback.invoke(success)
                if (success) {
                    userRepository.saveUser(userData, callback)
                }
            }
        )
    }
}