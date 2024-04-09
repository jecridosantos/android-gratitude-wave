package com.jdosantos.gratitudewavev1.app.usecase.notes

import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.repository.NoteRepository
import javax.inject.Inject

class GetMyNotesByTagUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {

    fun execute(tagId: String, callback: (List<Note>) -> Unit, onError: () -> Unit) {
        noteRepository.getMyNotesByTag(tagId) {
            callback(it)
        }

    }


}