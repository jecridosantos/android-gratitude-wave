package com.jdosantos.gratitudewavev1.ui.view.main.note.updatenote

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.handles.SingleLiveEvent
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GenerateByIAUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetNoteByIdUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.UpdateNoteUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.tags.GetTagsUseCase
import com.jdosantos.gratitudewavev1.utils.getCurrentDate
import com.jdosantos.gratitudewavev1.utils.publishingOptionLists
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class UpdateNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNoteByIdUseCase: GetNoteByIdUseCase?,
    private val updateNoteUseCase: UpdateNoteUseCase?,
    private val getTagsUseCase: GetTagsUseCase?,
    private val generateByIAUseCase: GenerateByIAUseCase
) :
    ViewModel() {

    private val tag = this::class.java.simpleName
    private val _tags = MutableStateFlow<List<NoteTag>>(emptyList())
    val tags: StateFlow<List<NoteTag>> = _tags

    var showDialogTag by mutableStateOf(false)

    var note by mutableStateOf(Note())
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    var showDialogEmotion by mutableStateOf(false)

    private var currentNoteType by mutableStateOf(publishingOptionLists[0])

    var showErrorForm by mutableStateOf(false)
    var showDialogColor by mutableStateOf(false)


    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> = _toastMessage

    init {
        getTagsUseCase!!.execute(callback = { tags ->
            _tags.value = tags
        }, onError = {
            Log.e(tag, "init - getTagsUseCase")
        })
    }

    fun getNoteById(id: String) {
        getNoteByIdUseCase!!.execute(id, { noteFound: Note ->
            note = noteFound
            currentNoteType = publishingOptionLists[noteFound.type!!]
        }) {
            Log.e(tag, "getNoteById - getNoteByIdUseCase")
        }
    }

    private val _saveResult = MutableLiveData<Result<Boolean>>()
    private val saveResult: LiveData<Result<Boolean>> = _saveResult
    fun updateNote() {

        if (note.note.isBlank()) {
            showErrorForm = true
            return
        }

        showErrorForm = false

        viewModelScope.launch {
            _saveResult.value = updateNoteUseCase!!.execute(note)
            saveResult.value!!.onSuccess {
                Log.d(tag, "updateNote - updateNoteUseCase")
            }.onFailure { exception ->
                _toastMessage.value = exception.message
            }
        }
    }

    fun onDate(value: Date) {
        note = note.copy(date = getCurrentDate(value))
    }

    fun onNote(value: String) {
        note = note.copy(note = value)
    }

    fun onEmotion(value: Int?) {
        if (value != null) {
            note = note.copy(emotion = value)
        }
    }

    fun clean() {
        note = Note()
    }

    fun showDialogEmotion() {
        showDialogEmotion = true
    }

    fun hideDialogEmotion() {
        showDialogEmotion = false
    }

    fun onTag(value: NoteTag?) {
        if (value?.id != "") {
            note = note.copy(noteTag = value)
        }
    }

    fun showDialogTag() {
        showDialogTag = true
    }

    fun hideDialogTag() {
        showDialogTag = false
    }

    fun showDialogColor() {
        showDialogColor = true
    }

    fun hideDialogColor() {
        showDialogColor = false
    }

    fun onColor(value: Int?) {
        note = note.copy(color = value)
    }

    fun generateMessage() {
        _isLoading.value = true
        viewModelScope.launch {
            generateByIAUseCase.generate(note.note)
                .onSuccess {
                    onNote(it)
                    _isLoading.value = false
                }
                .onFailure {
                    Log.e(tag, "generateMessage - generateByIAUseCase")
                    _isLoading.value = false
                    _toastMessage.value = it.message
                }
        }
    }

}