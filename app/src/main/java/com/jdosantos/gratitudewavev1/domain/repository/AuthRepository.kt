package com.jdosantos.gratitudewavev1.domain.repository

import com.google.firebase.auth.AuthCredential
import com.jdosantos.gratitudewavev1.domain.models.User

interface AuthRepository {

    suspend fun loginGoogle(credential: AuthCredential): Result<User>

    suspend fun sendEmailVerification(): Result<Boolean>

    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User>

    suspend fun register(email: String, password: String): Result<User>

    suspend fun reauthenticate(password: String): Result<User>

    fun isUserLoggedIn(): Result<Boolean>

    fun getCurrentUser(): Result<User>

    suspend fun signOut(): Result<Boolean>

    suspend fun resetPassword(email: String): Result<Boolean>
}