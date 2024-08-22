package com.jdosantos.gratitudewavev1.ui.view.main.note.writenote

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.data.local.NoteTypeStore
import com.jdosantos.gratitudewavev1.domain.handles.SingleLiveEvent
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GenerateByIAUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.SaveNoteUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.tags.GetTagsUseCase
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VALUE_INT_EMPTY
import com.jdosantos.gratitudewavev1.utils.convertToInt
import com.jdosantos.gratitudewavev1.utils.getCurrentDate
import com.jdosantos.gratitudewavev1.utils.publishingOptionLists
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class WriteNoteViewModel @Inject constructor(
    private val saveNoteUseCase: SaveNoteUseCase?,
    private val getTagsUseCase: GetTagsUseCase?,
    private val generateByIAUseCase: GenerateByIAUseCase
) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val tag = this::class.java.simpleName

    private val _noteTags = MutableStateFlow<List<NoteTag>>(emptyList())
    val noteTags: StateFlow<List<NoteTag>> = _noteTags

    private val _saveResult = MutableLiveData<Result<Boolean>>()
    private val saveResult: LiveData<Result<Boolean>> = _saveResult

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> = _toastMessage

    var note by mutableStateOf(Note())
        private set

    private var noteTypeDefault by mutableIntStateOf(0)

    private var currentNoteType by mutableStateOf(publishingOptionLists[0])


    var showDialogEmotion by mutableStateOf(false)

    var showDialogColor by mutableStateOf(false)

    var showDialogTag by mutableStateOf(false)
    var showErrorForm by mutableStateOf(false)
    suspend fun init(context: Context) {
        getTagsUseCase!!.execute(callback = { tags ->
            _noteTags.value = tags
        }, onError = {
            Log.e(tag, "init - getTagsUseCase")
        })

        val dataStore = NoteTypeStore(context)
        dataStore.getValue().collect { value ->
            noteTypeDefault = convertToInt(value)
            currentNoteType = publishingOptionLists[noteTypeDefault]
            onType(noteTypeDefault)
        }

    }


    fun saveNewNote() {
        if (note.note.isBlank()) {
            showErrorForm = true
            return
        }
        showErrorForm = false

        viewModelScope.launch {
            _saveResult.value = saveNoteUseCase!!.execute(note)
            saveResult.value!!.onSuccess {
                Log.d(tag, "saveNewNote - saveNoteUseCase")
            }.onFailure { exception ->
                _toastMessage.value = exception.message
            }
        }
    }

    fun onDate(value: Date) {
        note = note.copy(date = getCurrentDate(value))
    }

    fun onNote(value: String, generateByIA: Boolean = false) {
        note = note.copy(note = value, generatedByAI = generateByIA)
    }

    fun onEmotion(value: Int?) {
        if (value != null) {
            note = note.copy(emotion = value)
        }
    }

    fun onTag(value: NoteTag?) {
        if (value?.id != "") {
            note = note.copy(noteTag = value)
        }
    }

    private fun onType(value: Int) {
        if (value != VALUE_INT_EMPTY) {
            note = note.copy(type = value)
            currentNoteType = publishingOptionLists[value]
        }
    }

    fun clean() {
        note = Note()
        showErrorForm = false
    }

    fun showDialogEmotion() {
        showDialogEmotion = true
    }

    fun hideDialogEmotion() {
        showDialogEmotion = false
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