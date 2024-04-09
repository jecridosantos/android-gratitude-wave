package com.jdosantos.gratitudewavev1.ui.view.main.home.search

import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.model.Tag
import com.jdosantos.gratitudewavev1.app.usecase.notes.GetNotesByCurrentUserUseCase
import com.jdosantos.gratitudewavev1.app.usecase.GetTagsUseCase
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

    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags: StateFlow<List<Tag>> = _tags

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _notesData = MutableStateFlow<List<Note>>(emptyList())
    val notesData: StateFlow<List<Note>> = _notesData

    init {

        getTagsUseCase!!.execute() { tags ->
            _tags.value = tags
        }

        _isLoading.value = true
        getNotesByCurrentUserUseCase.execute() { notes ->
            _notesData.value = notes
            _isLoading.value = false
        }

    }
}