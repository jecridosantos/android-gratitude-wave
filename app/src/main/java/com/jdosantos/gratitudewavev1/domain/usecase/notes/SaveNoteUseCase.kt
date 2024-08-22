package com.jdosantos.gratitudewavev1.domain.usecase.notes

import com.jdosantos.gratitudewavev1.domain.exceptions.NoteException
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsAccount.Companion.LIMIT_NOTES_FOR_DAY
import com.jdosantos.gratitudewavev1.utils.getCurrentDate
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository
) {

    suspend fun execute(note: Note): Result<Boolean> {


        val noteCount = countNotesForToday()

        return if (noteCount < LIMIT_NOTES_FOR_DAY) {

            val email = authRepository.getCurrentUser().getOrNull()?.email
                ?: return Result.failure(NoteException.SaveError())

            var newNote = note.copy(email = email)
            if (note.date == "" ) {
                newNote = note.copy(date = getCurrentDate())
            }

            val success = saveNoteDeferred(newNote)
            Result.success(success)


        } else {
            Result.failure(NoteException.LimitForToday())
        }


    }

    private suspend fun countNotesForToday(): Int {
        val deferred = CompletableDeferred<Int>()
        noteRepository.countNotesForToday { noteCount ->
            deferred.complete(noteCount)
        }
        return deferred.await()
    }

    private suspend fun saveNoteDeferred(note: Note): Boolean {
        val deferredNote = CompletableDeferred<Boolean>()
        noteRepository.saveNote(note) { success ->
            deferredNote.complete(success)
        }
        return deferredNote.await()
    }
}