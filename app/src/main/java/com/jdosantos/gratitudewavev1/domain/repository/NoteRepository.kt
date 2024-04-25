package com.jdosantos.gratitudewavev1.domain.repository

import androidx.paging.PagingData
import com.jdosantos.gratitudewavev1.domain.models.Note
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface NoteRepository {
    fun getNotesByEmail(callback: (List<Note>) -> Unit, onError: ()-> Unit)

    fun saveNote(note: Note, callback: (success: Boolean) -> Unit)

    fun updateNote(note: Note, callback: (success: Boolean) -> Unit)

    fun getNoteById(id: String, callback: (note: Note) -> Unit, onError: () -> Unit)

    fun getNotesPaging(): Flow<PagingData<Note>>

    fun getMyNotesByDate(date: String, callback: (List<Note>) -> Unit, onError: ()-> Unit)

    fun deleteNoteById(id: String, callback: (success: Boolean) -> Unit)

    fun getMyNotesByTag(tagId: String, callback: (List<Note>) -> Unit, onError: ()-> Unit)

    fun getFirstNoteByEmail(callback: (Note?) -> Unit, onError: ()-> Unit)

    fun getNoteCreationDatesByEmail(callback: (List<Date>) -> Unit, onError: ()-> Unit)
}