package com.jdosantos.gratitudewavev1.ui.view.main.note.writenote

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.data.local.NoteTypeStore
import com.jdosantos.gratitudewavev1.domain.usecase.tags.GetTagsUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.SaveNoteUseCase
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VALUE_INT_EMPTY
import com.jdosantos.gratitudewavev1.utils.convertToInt
import com.jdosantos.gratitudewavev1.utils.publishingOptionLists
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteNoteViewModel @Inject constructor(
    private val saveNoteUseCase: SaveNoteUseCase?,
    private val getTagsUseCase: GetTagsUseCase?
) :
    ViewModel() {

    private val tag = this::class.java.simpleName

    private val _noteTags = MutableStateFlow<List<NoteTag>>(emptyList())
    val noteTags: StateFlow<List<NoteTag>> = _noteTags

    var note by mutableStateOf(Note())
        private set

    private var noteTypeDefault by mutableIntStateOf(0)

    private var currentNoteType by mutableStateOf(publishingOptionLists[0])

    private var showModalType by mutableStateOf(false)

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


    fun saveNewNote(callback: (success: Boolean) -> Unit) {
        showErrorForm = false
        if (note.note.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    saveNoteUseCase!!.execute(note, callback)
                } catch (e: Exception) {
                    Log.e(tag, "saveNewNote - saveNoteUseCase - error ${e.localizedMessage}")
                }
            }
        } else {
            callback.invoke(false)
            showErrorForm = true
        }
    }

    fun onNote(value: String) {
        note = note.copy(note = value)
    }

    fun onEmotion(value: Int?) {
        if (value != null) {
            note = note.copy(emotion = value)
        }
        //  hideDialogEmotion()
    }

    fun onTag(value: NoteTag?) {
        if (value?.id != "") {
            note = note.copy(noteTag = value)
        }
        //    hideDialogTag()

    }

    fun onType(value: Int) {
        if (value != VALUE_INT_EMPTY) {
            note = note.copy(type = value)
            currentNoteType = publishingOptionLists[value]
        }
        //     hideModalType()
    }

    fun clean() {
        note = Note()
        showErrorForm = false
    }


    fun showModalType() {
        showModalType = true
    }

    fun showDialogEmotion() {
        showDialogEmotion = true
    }

    fun hideModalType() {
        showModalType = false
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

        //  hideDialogColor()
    }


}