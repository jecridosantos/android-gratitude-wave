package com.jdosantos.gratitudewavev1.domain.usecase.auth

import android.util.Log
import com.jdosantos.gratitudewavev1.domain.exceptions.UserException
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    private val tag = this::class.java.simpleName
    suspend fun execute(): Result<User> {
        return try {
            val userByProvider = authRepository.getCurrentUser().getOrNull()
                ?: return Result.failure(UserException.UserNotFound())

            val userByUid = userRepository.getUserByUid(userByProvider.uid).getOrNull()

            val name = userByUid!!.name.takeIf { it.isNotEmpty() } ?: userByProvider.name

            val updatedUser = userByProvider.copy(name = name ?: return Result.failure(UserException.UserNameNotFound()), id = userByUid.id)

            Result.success(updatedUser)
        } catch (e: Exception) {
            Log.e(tag, "GetCurrentUserUseCase - error: ${e.message}")
            Result.failure(UserException.UserDefaultError())
        }
    }

}