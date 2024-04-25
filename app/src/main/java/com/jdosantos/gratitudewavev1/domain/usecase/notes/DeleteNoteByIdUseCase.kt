package com.jdosantos.gratitudewavev1.domain.usecase.notes

import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteByIdUseCase @Inject constructor(private val noteRepository: NoteRepository) {
    fun execute(id: String, callback: (success: Boolean) -> Unit) {
        noteRepository.deleteNoteById(id, callback)
    }
}