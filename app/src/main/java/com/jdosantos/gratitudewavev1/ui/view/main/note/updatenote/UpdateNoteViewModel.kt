package com.jdosantos.gratitudewavev1.ui.view.main.note.updatenote

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.domain.usecase.tags.GetTagsUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetNoteByIdUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.UpdateNoteUseCase
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.VALUE_INT_EMPTY
import com.jdosantos.gratitudewavev1.utils.publishingOptionLists
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateNoteViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase?,
    private val updateNoteUseCase: UpdateNoteUseCase?,
    private val getTagsUseCase: GetTagsUseCase?,
) :
    ViewModel() {
    private val tag = this::class.java.simpleName
    private val _tags = MutableStateFlow<List<NoteTag>>(emptyList())
    val tags: StateFlow<List<NoteTag>> = _tags

    var showDialogTag by mutableStateOf(false)

    var note by mutableStateOf(Note())
        private set

    private var showModalType by mutableStateOf(false)

    var showDialogEmotion by mutableStateOf(false)

    private var currentNoteType by mutableStateOf(publishingOptionLists[0])

    var showErrorForm by mutableStateOf(false)
    var showDialogColor by mutableStateOf(false)

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


    fun updateNote(callback: (Boolean) -> Unit) {
        showErrorForm = false
        if (note.note.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    updateNoteUseCase!!.execute(note, callback)

                } catch (e: Exception) {
                    Log.e(tag, "updateNote - updateNoteUseCase - error ${e.message}")
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
        //    hideDialogEmotion()
    }

    fun onType(value: Int) {
        if (value != VALUE_INT_EMPTY) {
            currentNoteType = publishingOptionLists[value]
            note = note.copy(type = value)
        }
        hideModalType()

    }

    fun clean() {
        note = Note()
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

    fun onTag(value: NoteTag?) {
        if (value?.id != "") {
            note = note.copy(noteTag = value)
        }
        //   hideDialogTag()

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

        //     hideDialogColor()
    }

}