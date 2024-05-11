package com.jdosantos.gratitudewavev1.domain.usecase.auth

import com.google.firebase.auth.AuthCredential
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    private val tag = this::class.java.simpleName
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
        callback: (isEmailVerified: Boolean) -> Unit,
        onError: () -> Unit
    ) {
        authRepository.signInWithEmailAndPassword(
            email,
            password,
            callback = { isEmailVerified ->
                callback.invoke(isEmailVerified)
            },
            onError
        )
    }

    suspend fun login(
        email: String,
        password: String
    ) {
        authRepository.login(
            email,
            password
        )
    }


    suspend fun signInWithGoogle(
        credential: AuthCredential,
        callback: (success: Boolean) -> Unit
    ) {
        authRepository.signInWithGoogle(
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
}