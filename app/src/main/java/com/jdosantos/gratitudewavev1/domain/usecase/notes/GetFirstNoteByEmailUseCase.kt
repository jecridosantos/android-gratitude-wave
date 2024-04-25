package com.jdosantos.gratitudewavev1.domain.usecase.notes

import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import javax.inject.Inject

class GetFirstNoteByEmailUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {

    fun execute(callback: (Note?) -> Unit, onError: () -> Unit) {
        noteRepository.getFirstNoteByEmail(callback, onError)

    }
}