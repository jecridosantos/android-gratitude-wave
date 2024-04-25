package com.jdosantos.gratitudewavev1.domain.usecase.auth

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    private val tag = this::class.java.simpleName
    suspend fun execute(
        email: String,
        password: String,
        callback: (isEmailVerified: Boolean) -> Unit,
        onError: () -> Unit
    ) {
        authRepository.login(
            email,
            password,
            callback = { isEmailVerified ->
                callback.invoke(isEmailVerified)
            },
            onError
        )
    }

    suspend fun signInWithGoogle(
        credential: AuthCredential,
        callback: (success: Boolean) -> Unit
    ) {
        authRepository.loginGoogle(
            credential = credential,
            callback = { userLogged ->
                userRepository.getUserByUid(
                    uid = userLogged.uid,
                    callback = { user ->
                        if (user!!.uid == userLogged.uid) {
                            callback.invoke(true)
                        } else {
                            userRepository.saveUser(userLogged, callback)
                        }
                    },
                    onError = { callback.invoke(false) }
                )
            },
            onError = {
                callback.invoke(false)
            })
    }

    fun isLogged(): Boolean {
        return try {
            val loggedUser = authRepository.loggedUser()
            Log.d(tag, "isLogged - uid: ${loggedUser.uid}")
            loggedUser.uid!!.isNotEmpty()
        } catch (e: Exception) {
            Log.e(tag, "isLogged - error: ${e.message}")
            false
        }
    }
}