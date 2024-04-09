package com.jdosantos.gratitudewavev1.app.usecase

import android.util.Log
import com.jdosantos.gratitudewavev1.app.model.User
import com.jdosantos.gratitudewavev1.app.repository.AuthRepository
import com.jdosantos.gratitudewavev1.app.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(user: User, password: String, onSuccess: () -> Unit, onError: () -> Unit) {
        authRepository.signUp(user.email, password, {
            userRepository.saveUser(user, onSuccess) { error: String ->
                Log.d("SignUpUseCase", error)
                onError()
            }
        }, onError)
    }

    fun sendEmailVerification(onSuccess: () -> Unit, onError: () -> Unit) {
        authRepository.sendEmailVerification(onSuccess, onError)
    }
}