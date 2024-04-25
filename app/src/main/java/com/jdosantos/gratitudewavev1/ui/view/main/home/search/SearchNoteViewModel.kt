package com.jdosantos.gratitudewavev1.ui.view.main.home.search

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetNotesByCurrentUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.tags.GetTagsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchNoteViewModel @Inject constructor(
    getNotesByCurrentUserUseCase: GetNotesByCurrentUserUseCase,
    private val getTagsUseCase: GetTagsUseCase?
) :
    ViewModel() {
    private val tag = this::class.java.simpleName
    private val _tags = MutableStateFlow<List<NoteTag>>(emptyList())
    val tags: StateFlow<List<NoteTag>> = _tags

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _notesData = MutableStateFlow<List<Note>>(emptyList())
    val notesData: StateFlow<List<Note>> = _notesData

    init {

        getTagsUseCase!!.execute(callback = { tags ->
            _tags.value = tags
        }, onError = {
            Log.e(tag, "init - getTagsUseCase")
        })

        _isLoading.value = true
        getNotesByCurrentUserUseCase.execute(callback = { notes ->
            _notesData.value = notes
            _isLoading.value = false
        }, onError = {
            Log.e(tag, "init - getNotesByCurrentUserUseCase")
        })

    }
}