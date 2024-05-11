package com.jdosantos.gratitudewavev1.domain.usecase.auth

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.jdosantos.gratitudewavev1.domain.exceptions.AuthenticationException
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    private val tag = this::class.java.simpleName

    suspend fun login(email: String, password: String): Result<User> {
        return authRepository.signInWithEmailAndPassword(email, password)
    }


    suspend fun signInWithGoogle(
        credential: AuthCredential
    ): Result<User> {
        try {
            return authRepository.loginGoogle(credential).onSuccess { user ->
                userRepository.getUserByUid(user.uid).onSuccess {
                    return Result.success(user)
                }.onFailure {
                    userRepository.saveUser(user).onSuccess {
                        return Result.success(user)
                    }.onFailure {
                        return Result.failure(AuthenticationException.GenericAuthenticationException())
                    }
                }
            }.onFailure {
                return Result.failure(AuthenticationException.GenericAuthenticationException())
            }
        } catch (e: Exception) {
            Log.e(tag, "signInWithGoogle - error: ${e.message}")
            return Result.failure(AuthenticationException.GenericAuthenticationException())
        }
    }

    fun isUserLoggedIn(): Result<Boolean> {
        return authRepository.isUserLoggedIn();
    }
}