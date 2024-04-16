package com.jdosantos.gratitudewavev1.ui.view.main.note.updatenote

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.core.common.util.noteTypeConfigLists
import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.model.Tag
import com.jdosantos.gratitudewavev1.app.usecase.notes.GetNoteByIdUseCase
import com.jdosantos.gratitudewavev1.app.usecase.GetTagsUseCase
import com.jdosantos.gratitudewavev1.app.usecase.notes.UpdateNoteUseCase
import com.jdosantos.gratitudewavev1.core.common.constants.Constants.Companion.VALUE_INT_EMPTY
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

    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags: StateFlow<List<Tag>> = _tags

    var showDialogTag by mutableStateOf(false)

    var note by mutableStateOf(Note())
        private set

    var showModalType by mutableStateOf(false)

    var showDialogEmotion by mutableStateOf(false)

    var currentNoteType by mutableStateOf(noteTypeConfigLists[0])

    var showErrorForm by mutableStateOf(false)
    var showDialogColor by mutableStateOf(false)
    init {
        getTagsUseCase!!.execute() { tags ->
            _tags.value = tags
        }
    }

    fun getNoteById(id: String) {
        getNoteByIdUseCase!!.execute(id, { noteFound: Note ->

            /*         note = note.copy(
                         idDoc = noteFound.idDoc,
                         note = noteFound.note,
                         emotion = noteFound.emotion,
                         date = noteFound.date,
                         email = noteFound.email,
                         type = noteFound.type,
                         updateAt = noteFound.updateAt
                     )
         */
            note = noteFound
            currentNoteType = noteTypeConfigLists[noteFound.type!!]
        }) { it ->
            Log.d("Error", it)
        }
    }


    fun updateNote(onSuccess: () -> Unit, onError: () -> Unit) {
        showErrorForm = false
        if (note.note.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    /*         val newNote: Note = Note().copy(
                             note = note.note,
                             type = note.type,
                             emotion = note.emotion,
                             idDoc = note.idDoc
                         )*/

                    updateNoteUseCase!!.execute(note, {
                        Log.d("SUCCESS UPDATE", "Se actualizó exitosamente")
                        onSuccess()
                    }) { error ->
                        Log.d("ERROR UPDATE", error)
                    }

                } catch (e: Exception) {
                    Log.d("ERROR UPDATE", "Error at update new note ${e.localizedMessage}")
                }
            }
        } else {
            onError()
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
        if (value !=VALUE_INT_EMPTY) {
            currentNoteType = noteTypeConfigLists[value]
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

    fun onTag(value: Tag?) {
        if (value?.id != "") {
            note = note.copy(tag = value)
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