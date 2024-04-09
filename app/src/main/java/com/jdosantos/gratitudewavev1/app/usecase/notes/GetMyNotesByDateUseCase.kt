package com.jdosantos.gratitudewavev1.app.usecase.notes

import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.repository.NoteRepository
import javax.inject.Inject

class GetMyNotesByDateUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {

    fun execute(date: String, callback: (List<Note>) -> Unit) {
        noteRepository.getMyNotesByDate(date) {
            callback(it)
        }

    }


}