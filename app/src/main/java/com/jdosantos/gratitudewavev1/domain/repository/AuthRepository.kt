package com.jdosantos.gratitudewavev1.domain.repository

import com.google.firebase.auth.AuthCredential
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.models.UserData
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun get(): Flow<User>

    fun getCurrentUser(callback: (UserData) -> Unit, onError: () -> Unit)

    suspend fun login(email: String, password: String)

    suspend fun signInWithEmailAndPassword(email: String, password: String, callback: (isEmailVerified: Boolean) -> Unit, onError: () -> Unit)

    fun logout(callback: (success: Boolean) -> Unit)

    suspend fun signInWithGoogle(credential: AuthCredential, callback: (UserData) -> Unit, onError: () -> Unit)

    suspend fun createUserWithEmailAndPassword(email: String, password: String, callback: (success: Boolean) -> Unit)

    fun sendEmailVerification(callback: (success: Boolean) -> Unit)
}