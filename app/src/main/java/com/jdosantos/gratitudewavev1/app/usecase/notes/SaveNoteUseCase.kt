package com.jdosantos.gratitudewavev1.app.usecase.notes

import com.jdosantos.gratitudewavev1.core.common.util.getCurrentDate
import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.repository.AuthRepository
import com.jdosantos.gratitudewavev1.app.repository.NoteRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository
) {

    fun execute(note: Note, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
        val email = authRepository.loggedUser().email

        val newNote = note.copy(email = email!!, date = getCurrentDate())

        noteRepository.saveNote(newNote, onSuccess, onError)
    }
}