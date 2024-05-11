package com.jdosantos.gratitudewavev1.domain.usecase.auth

import com.jdosantos.gratitudewavev1.domain.exceptions.GenericException
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(user: User, password: String): Result<Boolean> {
        return authRepository.register(
            user.email,
            password
        ).map {
            return userRepository.saveUser(user).map {
                return Result.success(true)
            }
        }.onFailure {
            return Result.failure(it)
        }
    }
}