package com.jdosantos.gratitudewavev1.app.repository

import androidx.paging.PagingData
import com.jdosantos.gratitudewavev1.app.model.Note
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface NoteRepository {
    fun getNotesByEmail(callback: (List<Note>) -> Unit)

    fun saveNote(note: Note, onSuccess: () -> Unit, onError: (error: String) -> Unit)

    fun updateNote(note: Note, onSuccess: () -> Unit, onError: (error: String) -> Unit)

    fun getNoteById(id: String, onSuccess: (note: Note) -> Unit, onError: (error: String) -> Unit)

    fun getNotesPaging(): Flow<PagingData<Note>>

    fun getMyNotesByDate(date: String, callback: (List<Note>) -> Unit)

    fun deleteNoteById(id: String, onSuccess: () -> Unit, onError: (error: String) -> Unit)

    fun getMyNotesByTag(tagId: String, callback: (List<Note>) -> Unit)

    fun getFirstNoteByEmail(callback: (Note?) -> Unit, onError: (error: String)-> Unit)

    fun getNoteCreationDatesByEmail(callback: (List<Date>) -> Unit, onError: (error: String)-> Unit)
}