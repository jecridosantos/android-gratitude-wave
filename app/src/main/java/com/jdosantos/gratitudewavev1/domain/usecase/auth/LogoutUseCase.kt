package com.jdosantos.gratitudewavev1.domain.usecase.auth

import android.util.Log
import com.jdosantos.gratitudewavev1.data.local.CredentialStore
import com.jdosantos.gratitudewavev1.data.local.NoteGenerationTracker
import com.jdosantos.gratitudewavev1.data.local.NoteTypeStore
import com.jdosantos.gratitudewavev1.data.local.RemindersStore
import com.jdosantos.gratitudewavev1.data.local.UserPreferencesManager
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val credentialStore: CredentialStore,
    private val noteGenerationTracker: NoteGenerationTracker,
    private val noteTypeStore: NoteTypeStore,
    private val preferencesManager: UserPreferencesManager,
    private val remindersStore: RemindersStore,
    private val userPreferencesManager: UserPreferencesManager
) {
    private val tag = this::class.java.simpleName
    suspend fun execute(): Result<Boolean> {
        try {
            credentialStore.clearAll()
            noteGenerationTracker.clearAll()
            noteTypeStore.clearAll()
            preferencesManager.clearAll()
            remindersStore.clearAll()
            userPreferencesManager.clearAll()
            return authRepository.signOut()
        } catch (e: Exception) {
            Log.e(tag, "LogoutUseCase - error: ${e.message}")
            return Result.success(false)
        }


    }
}