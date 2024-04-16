package com.jdosantos.gratitudewavev1.ui.view.main.note.writenote

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.core.common.util.noteTypeConfigLists
import com.jdosantos.gratitudewavev1.core.common.util.convertToInt
import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.model.Tag
import com.jdosantos.gratitudewavev1.app.store.NoteTypeStore
import com.jdosantos.gratitudewavev1.app.usecase.GetTagsUseCase
import com.jdosantos.gratitudewavev1.app.usecase.notes.SaveNoteUseCase
import com.jdosantos.gratitudewavev1.core.common.constants.Constants.Companion.VALUE_INT_EMPTY
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

    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags: StateFlow<List<Tag>> = _tags

    var note by mutableStateOf(Note())
        private set

    private var noteTypeDefault by mutableIntStateOf(0)

    private var currentNoteType by mutableStateOf(noteTypeConfigLists[0])

    private var showModalType by mutableStateOf(false)

    var showDialogEmotion by mutableStateOf(false)

    var showDialogColor by mutableStateOf(false)

    var showDialogTag by mutableStateOf(false)
    var showErrorForm by mutableStateOf(false)
    suspend fun init(context: Context) {

        getTagsUseCase!!.execute() { tags ->
            _tags.value = tags
        }

        val dataStore = NoteTypeStore(context)
        dataStore.getValue().collect { value ->
            noteTypeDefault = convertToInt(value)
            currentNoteType = noteTypeConfigLists[noteTypeDefault]
            onType(noteTypeDefault)
        }


    }


    fun saveNewNote(onSuccess: () -> Unit, onError: () -> Unit) {
        showErrorForm = false
        if (note.note.isNotBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    /*              val newNote: Note = Note().copy(
                    note = note.note,
                    type = note.type,
                    emotion = note.emotion
                )
*/

                    saveNoteUseCase!!.execute(note, {
                        Log.d("SUCCESS SAVE", "Se guardo exitosamente")
                        onSuccess()
                    }) { error ->
                        Log.d("ERROR SAVE", error)
                    }


                } catch (e: Exception) {
                    Log.d("ERROR SAVE", "Error at save new note ${e.localizedMessage}")
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
      //  hideDialogEmotion()
    }

    fun onTag(value: Tag?) {
        if (value?.id != "") {
            note = note.copy(tag = value)
        }
        //    hideDialogTag()

    }

    fun onType(value: Int) {
        if (value != VALUE_INT_EMPTY) {
            note = note.copy(type = value)
            currentNoteType = noteTypeConfigLists[value]
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