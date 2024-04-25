package com.jdosantos.gratitudewavev1.domain.usecase.notes

import com.jdosantos.gratitudewavev1.utils.getCurrentDate
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository
) {

    fun execute(note: Note, callback: (success: Boolean) -> Unit) {
        val email = authRepository.loggedUser().email

        val newNote = note.copy(email = email!!, date = getCurrentDate())

        noteRepository.saveNote(newNote, callback)
    }
}