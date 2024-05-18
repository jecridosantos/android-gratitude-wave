package com.jdosantos.gratitudewavev1.ui.view.main.note.sharednote

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.GetCurrentUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.DeleteNoteByIdUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetNoteByIdUseCase
import com.jdosantos.gratitudewavev1.utils.constants.ConstantsRouteParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedNoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNoteByIdUseCase: GetNoteByIdUseCase?,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) :
    ViewModel() {

    val id: String = checkNotNull(savedStateHandle[ConstantsRouteParams.NOTE_DETAILS_ID]?:"")

    val color: String = checkNotNull(savedStateHandle[ConstantsRouteParams.NOTE_DETAILS_COLOR]?:"0")

    private val tag = this::class.java.simpleName
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

    var user by mutableStateOf(User())
        private set

    fun getCurrentUser() {
        getCurrentUserUseCase.execute().onSuccess { value: User ->
            user = value
        }
    }

}