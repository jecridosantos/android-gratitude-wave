package com.jdosantos.gratitudewavev1.domain.usecase.auth

import com.jdosantos.gratitudewavev1.domain.models.UserData
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    fun execute(callback: (UserData) -> Unit, onError: () -> Unit) {
        authRepository.getCurrentUser(
            callback = { user ->
                userRepository.getUserByUid(
                    uid = user.uid,
                    callback = { userFound ->
                        callback.invoke(userFound!!)
                    },
                    onError = {
                        onError.invoke()
                    })
            }, onError
        )
    }
}