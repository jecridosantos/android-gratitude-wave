package com.jdosantos.gratitudewavev1.domain.usecase.auth

import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) {

    suspend fun execute(email: String): Result<Boolean> {
       /* return userRepository.emailExists(email).onSuccess {
            exists ->
            if (exists) {
                return authRepository.resetPassword(email)
            }
        }*/

        val emailExistsResult = userRepository.emailExists(email)
        return if (emailExistsResult.isSuccess && emailExistsResult.getOrNull() == true) {
            authRepository.resetPassword(email)
        } else {
            Result.failure(emailExistsResult.exceptionOrNull()!!)
        }

    }
}