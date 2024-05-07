package com.jdosantos.gratitudewavev1.domain.repository

import com.google.firebase.auth.AuthCredential
import com.jdosantos.gratitudewavev1.domain.models.LoggedUser
import com.jdosantos.gratitudewavev1.domain.models.User

interface AuthRepository {


    fun getCurrentUser(callback: (User) -> Unit, onError: () -> Unit)

    suspend fun login(email: String, password: String, callback: (isEmailVerified: Boolean) -> Unit, onError: () -> Unit)

    fun logout(callback: (success: Boolean) -> Unit)

    fun loggedUser(): LoggedUser

    suspend fun loginGoogle(credential: AuthCredential, callback: (User) -> Unit, onError: () -> Unit)

    suspend fun signUp(email: String, password: String, callback: (success: Boolean) -> Unit)

    fun sendEmailVerification(callback: (success: Boolean) -> Unit)

    // Cambios mejorados
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User>

    suspend fun reauthenticate(password: String): Result<User>
    fun isUserLoggedIn(): Result<Boolean>

    fun getCurrentUser(): Result<User>
    suspend fun signOut()
}