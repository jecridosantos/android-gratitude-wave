package com.jdosantos.gratitudewavev1.app.usecase

import com.jdosantos.gratitudewavev1.app.model.User
import com.jdosantos.gratitudewavev1.app.repository.AuthRepository
import com.jdosantos.gratitudewavev1.app.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    fun execute(onSuccess: (User) -> Unit, onError: () -> Unit) {
        authRepository.getCurrentUser({
            userRepository.getUserByUid(it.uid, { userFound ->
                onSuccess(userFound!!)
            }) {
                onError()
            }
        }, onError)
    }
}