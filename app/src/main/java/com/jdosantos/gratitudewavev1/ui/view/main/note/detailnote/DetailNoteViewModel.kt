package com.jdosantos.gratitudewavev1.ui.view.main.note.detailnote

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.usecase.notes.DeleteNoteByIdUseCase
import com.jdosantos.gratitudewavev1.app.usecase.notes.GetNoteByIdUseCase
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
    var showAlert by mutableStateOf(false)

    var note by mutableStateOf(Note())
        private set

    fun getNoteById(id: String, reload: (Int?) -> Unit) {
        getNoteByIdUseCase!!.execute(id, { note: Note ->
            this.note = note
            reload(note.color)
        }) { it ->
            Log.d("Error", it)
        }
    }

    fun clean() {
        note = Note()
    }

    fun delete(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            deleteNoteByIdUseCase!!.execute(id, onSuccess) {
                Log.d("Error", it)
            }
        }
    }

    fun showAlert() {
        showAlert = true
    }

    fun closeAlert() {
        showAlert = false
    }
}