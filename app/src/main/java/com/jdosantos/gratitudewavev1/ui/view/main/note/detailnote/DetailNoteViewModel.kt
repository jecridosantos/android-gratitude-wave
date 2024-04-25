package com.jdosantos.gratitudewavev1.ui.view.main.note.detailnote

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.usecase.notes.DeleteNoteByIdUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetNoteByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailNoteViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase?,
    private val deleteNoteByIdUseCase: DeleteNoteByIdUseCase?
) :
    ViewModel() {

    private val tag = this::class.java.simpleName

    var showAlert by mutableStateOf(false)

    var note by mutableStateOf(Note())
        private set

    fun getNoteById(id: String, reload: (Int?) -> Unit) {
        getNoteByIdUseCase!!.execute(
            id,
            callback = { note: Note ->
                this.note = note
                reload(note.color)
            }, onError = {
                Log.e(tag, "getNoteById - getNoteByIdUseCase")
            })
    }

    fun clean() {
        note = Note()
    }

    fun delete(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            deleteNoteByIdUseCase!!.execute(id,
                callback = { success ->
                    if (success) {
                        onSuccess.invoke()
                    } else {
                        Log.e(tag, "delete - deleteNoteByIdUseCase")
                    }
                })
        }
    }

    fun showAlert() {
        showAlert = true
    }

    fun closeAlert() {
        showAlert = false
    }
}