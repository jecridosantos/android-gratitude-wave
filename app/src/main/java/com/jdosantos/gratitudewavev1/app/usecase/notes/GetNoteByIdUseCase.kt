package com.jdosantos.gratitudewavev1.app.usecase.notes

import android.util.Log
import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.repository.NoteRepository
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) {

    fun execute(id: String, callback: (Note) -> Unit, error: (String) -> Unit) {
        Log.d("Llamada 2", "GetNoteByIdUseCase")
        noteRepository.getNoteById(id, {
            callback(it)
        }) {
            error(it)
        }

    }
}