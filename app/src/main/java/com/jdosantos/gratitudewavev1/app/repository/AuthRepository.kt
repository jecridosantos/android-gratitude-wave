package com.jdosantos.gratitudewavev1.app.repository

import com.google.firebase.auth.AuthCredential
import com.jdosantos.gratitudewavev1.app.model.LoggedUser
import com.jdosantos.gratitudewavev1.app.model.User

interface AuthRepository {

    fun getCurrentUser(onSuccess: (User) -> Unit, onError: () -> Unit)

    suspend fun login(email: String, password: String, onSuccess: (isEmailVerified: Boolean) -> Unit, onError: () -> Unit)

    fun logout(onSuccess: () -> Unit, onError: () -> Unit)

    fun loggedUser(): LoggedUser

    suspend fun loginGoogle(credential: AuthCredential, onSuccess: (User) -> Unit, onError: () -> Unit)

    suspend fun signUp(email: String, password: String, onSuccess: () -> Unit, onError: () -> Unit)

    fun sendEmailVerification(onSuccess: () -> Unit, onError: () -> Unit)
}