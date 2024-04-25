package com.jdosantos.gratitudewavev1.domain.usecase.notes

import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    fun execute(id: String, callback: (Note) -> Unit, onError: () -> Unit) {
        noteRepository.getNoteById(id, callback, onError)
    }
}