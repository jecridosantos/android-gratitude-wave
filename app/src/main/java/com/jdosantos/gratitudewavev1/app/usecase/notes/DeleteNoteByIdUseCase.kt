package com.jdosantos.gratitudewavev1.app.usecase.notes

import com.jdosantos.gratitudewavev1.app.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteByIdUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    fun execute(id: String, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
        noteRepository.deleteNoteById(id, onSuccess, onError)
    }
}