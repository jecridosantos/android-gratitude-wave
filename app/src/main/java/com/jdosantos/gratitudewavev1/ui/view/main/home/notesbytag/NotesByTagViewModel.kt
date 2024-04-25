package com.jdosantos.gratitudewavev1.ui.view.main.home.notesbytag

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.domain.usecase.tags.GetTagByIdUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetMyNotesByTagUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NotesByTagViewModel @Inject constructor(
    private val getMyNotesByTagUseCase: GetMyNotesByTagUseCase,
    private val getTagByIdUseCase: GetTagByIdUseCase
) :
    ViewModel() {
    private val tag = this::class.java.simpleName
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    var noteTag by mutableStateOf(NoteTag())
        private set

    fun fetchNotes(tagId: String) {
        _isLoading.value = true

        getTagByIdUseCase.execute(tagId, { noteTag: NoteTag ->
            this.noteTag = noteTag
        }) {
            Log.e(tag, "fetchNotes - getTagByIdUseCase")
        }

        getMyNotesByTagUseCase.execute(tagId, { notes ->
            _notes.value = notes
            _isLoading.value = false
        }) {
            _isLoading.value = false
            Log.e(tag, "fetchNotes - getMyNotesByTagUseCase")
        }

    }
}