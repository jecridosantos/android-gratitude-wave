package com.jdosantos.gratitudewavev1.app.usecase.notes

import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {

    fun execute(note: Note, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
        noteRepository.updateNote(note, onSuccess, onError)
    }
}