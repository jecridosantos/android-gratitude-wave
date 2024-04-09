package com.jdosantos.gratitudewavev1.app.usecase.notes

import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.repository.NoteRepository
import javax.inject.Inject

class GetFirstNoteByEmailUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {

    fun execute(callback: (Note?) -> Unit, error: (String) -> Unit) {
        noteRepository.getFirstNoteByEmail({
            callback(it)
        }) {
            error(it)
        }

    }
}