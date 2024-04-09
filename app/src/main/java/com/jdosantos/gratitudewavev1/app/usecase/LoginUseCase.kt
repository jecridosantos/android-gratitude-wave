package com.jdosantos.gratitudewavev1.app.usecase

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.jdosantos.gratitudewavev1.app.repository.AuthRepository
import com.jdosantos.gratitudewavev1.app.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(
        email: String,
        password: String,
        onSuccess: (isEmailVerified: Boolean) -> Unit,
        onError: () -> Unit
    ) {
        authRepository.login(email, password, { isEmailVerified ->
            onSuccess(isEmailVerified)
        }, onError)
    }

    suspend fun signInWithGoogle(
        credential: AuthCredential,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        authRepository.loginGoogle(credential, { userLogged ->
            userRepository.getUserByUid(userLogged.uid, {
                if (it!!.uid == userLogged.uid) {
                    onSuccess()
                } else {
                    userRepository.saveUser(userLogged, {
                        onSuccess()
                    }) {
                        onError()
                    }
                }
            }) {
                onError()
            }
        }, onError)
    }

    fun isLogged(): Boolean {
        return try {
            val loggedUser = authRepository.loggedUser()
            Log.d("LoginUseCase", "uid: ${loggedUser.uid}")
            loggedUser.uid!!.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }
}