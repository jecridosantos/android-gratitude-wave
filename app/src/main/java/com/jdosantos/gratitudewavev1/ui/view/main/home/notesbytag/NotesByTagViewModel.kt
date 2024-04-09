package com.jdosantos.gratitudewavev1.ui.view.main.home.notesbytag

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.model.Tag
import com.jdosantos.gratitudewavev1.app.usecase.notes.GetMyNotesByTagUseCase
import com.jdosantos.gratitudewavev1.app.usecase.GetTagByIdUseCase
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
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    var tag by mutableStateOf(Tag())
        private set

    fun fetchNotes(tagId: String) {
        _isLoading.value = true

        getTagByIdUseCase.execute(tagId, { tag: Tag ->
            this.tag = tag
        }) { it ->
            Log.d("Error", it)
        }


        getMyNotesByTagUseCase.execute(tagId,{ notes ->
            _notes.value = notes
            _isLoading.value = false
        } ) {
            _isLoading.value = false
        }

    }
}