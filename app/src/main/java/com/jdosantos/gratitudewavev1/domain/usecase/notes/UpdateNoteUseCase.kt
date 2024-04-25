package com.jdosantos.gratitudewavev1.domain.usecase.notes

import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    fun execute(note: Note, callback: (success: Boolean) -> Unit) {
        noteRepository.updateNote(note, callback)
    }
}