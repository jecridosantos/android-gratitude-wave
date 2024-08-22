package com.jdosantos.gratitudewavev1.domain.usecase.notes

import com.google.firebase.vertexai.GenerativeModel
import com.jdosantos.gratitudewavev1.data.local.NoteGenerationTracker
import com.jdosantos.gratitudewavev1.data.local.UserPreferencesManager
import com.jdosantos.gratitudewavev1.domain.exceptions.NoteException
import com.jdosantos.gratitudewavev1.domain.handles.LocalizedMessageManager
import com.jdosantos.gratitudewavev1.domain.handles.NoteGenerationResult
import com.jdosantos.gratitudewavev1.domain.models.UserPreferences
import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsAccount.Companion.LIMIT_NOTES_BY_IA_FOR_DAY
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class GenerateByIAUseCase @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val noteRepository: NoteRepository,
    private val noteGenerationTracker: NoteGenerationTracker,
    private val userPreferencesManager: UserPreferencesManager,
    private val localizedMessageManager: LocalizedMessageManager
) {
    suspend fun generate(inputPrompt: String? = null): Result<String> {
        when (val generationResult = noteGenerationTracker.canGenerateNote()) {
            is NoteGenerationResult.CanGenerate -> {
                val noteCountByIA = countNotesByIAForToday()

                return if (noteCountByIA >= LIMIT_NOTES_BY_IA_FOR_DAY) {
                    Result.failure(NoteException.LimitByIAForToday())
                } else {
                    try {

                        val retrievedUserPreferences = userPreferencesManager.getUserPreferences()
                        val prompt = createPersonalizedPrompt(retrievedUserPreferences, inputPrompt)
                        val response = generativeModel.generateContent(prompt)
                        val responseText =
                            response.text ?: return Result.failure(NoteException.GenerateError())
                        val cleanText = responseText.replace("\"", "").replace("*", "")

                        noteGenerationTracker.incrementNoteCount()

                        Result.success(cleanText)
                    } catch (e: Exception) {
                        Result.failure(NoteException.GenerateError())
                    }
                }
            }

            is NoteGenerationResult.MustWait -> {
                val minutesToWait = generationResult.waitTime / 1000 / 60
                return Result.failure(NoteException.WaitTime(minutesToWait))
            }

            is NoteGenerationResult.DailyLimitReached -> {
                return Result.failure(NoteException.LimitGenerateByIAForToday())
            }
        }
    }

    private suspend fun countNotesByIAForToday(): Int {
        val deferredByIA = CompletableDeferred<Int>()
        noteRepository.countNotesByIAForToday { noteCount ->
            deferredByIA.complete(noteCount)
        }
        return deferredByIA.await()
    }

    private fun createPersonalizedPrompt(
        userPreferences: UserPreferences,
        inputPrompt: String? = null
    ): String {

        val parts = mutableListOf<String>()

        if (userPreferences.age.isNotEmpty()) {
            parts.add("${localizedMessageManager.getMessage(LocalizedMessageManager.MessageKey.PROMPT_MESSAGE_AGE)} ${userPreferences.age}")
        }
        if (userPreferences.country.isNotEmpty()) {
            parts.add("${localizedMessageManager.getMessage(LocalizedMessageManager.MessageKey.PROMPT_MESSAGE_COUNTRY)} ${userPreferences.country}")
        }
        if (userPreferences.profession.isNotEmpty()) {
            parts.add("${localizedMessageManager.getMessage(LocalizedMessageManager.MessageKey.PROMPT_MESSAGE_PROFESSION)} ${userPreferences.profession}")
        }
        if (userPreferences.selectedInterests.isNotEmpty() || userPreferences.interests.isNotEmpty()) {
            val interests = if (userPreferences.selectedInterests.isNotEmpty()) {
                userPreferences.selectedInterests.random()
            } else {
                userPreferences.interests
            }
            parts.add("${localizedMessageManager.getMessage(LocalizedMessageManager.MessageKey.PROMPT_MESSAGE_INTERESTS)} $interests")
        }
        if (userPreferences.selectedRelationships.isNotEmpty() || userPreferences.relationship.isNotEmpty()) {
            val relationships = if (userPreferences.selectedRelationships.isNotEmpty()) {
                userPreferences.selectedRelationships.random()
            } else {
                userPreferences.relationship
            }
            parts.add("${localizedMessageManager.getMessage(LocalizedMessageManager.MessageKey.PROMPT_MESSAGE_RELATIONSHIPS)} $relationships")
        }
        if (userPreferences.about.isNotEmpty()) {
            parts.add("${localizedMessageManager.getMessage(LocalizedMessageManager.MessageKey.PROMPT_MESSAGE_ABOUT)} ${userPreferences.about}")
        }

        // Crea el prompt con la instrucción
        return "${localizedMessageManager.getMessage(LocalizedMessageManager.MessageKey.PROMPT_MESSAGE_RANDOM_PROMPT_STARTER)} $inputPrompt ${
            localizedMessageManager.getMessage(
                LocalizedMessageManager.MessageKey.PROMPT_MESSAGE_FINAL_PROMPT
            )
        }"
    }

}