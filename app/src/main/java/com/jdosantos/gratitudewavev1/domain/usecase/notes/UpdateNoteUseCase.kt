package com.jdosantos.gratitudewavev1.domain.usecase.notes

import com.jdosantos.gratitudewavev1.domain.exceptions.NoteException
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
//    fun execute(note: Note, callback: (success: Boolean) -> Unit) {
//        noteRepository.updateNote(note, callback)
//    }

    suspend fun execute(note: Note): Result<Boolean> {

        return try {

            val success = saveNoteDeferred(note)
            Result.success(success)

        } catch (e: Exception) {
            Result.failure(NoteException.GenerateError())
        }

    }

    private suspend fun saveNoteDeferred(note: Note): Boolean {
        val deferredNote = CompletableDeferred<Boolean>()
        noteRepository.updateNote(note) { success ->
            deferredNote.complete(success)
        }
        return deferredNote.await()
    }
}