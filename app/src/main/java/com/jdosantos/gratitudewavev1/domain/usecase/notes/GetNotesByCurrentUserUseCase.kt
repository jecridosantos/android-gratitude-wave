package com.jdosantos.gratitudewavev1.domain.usecase.notes

import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import javax.inject.Inject

class GetNotesByCurrentUserUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {
    fun execute(callback: (List<Note>) -> Unit, onError: ()-> Unit) {
        noteRepository.getNotesByEmail(callback, onError)
    }

}