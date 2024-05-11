package com.jdosantos.gratitudewavev1.ui.view.main.home

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberImagePainter
import com.jdosantos.gratitudewavev1.R
import com.jdosantos.gratitudewavev1.domain.enums.TimeOfDay
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.GetCurrentUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetMyNotesByDateUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetNotesByCurrentUserUseCase
import com.jdosantos.gratitudewavev1.utils.getCurrentDate
import com.jdosantos.gratitudewavev1.utils.getTimeOfDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMyNotesByDateUseCase: GetMyNotesByDateUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getNotesByCurrentUserUseCase: GetNotesByCurrentUserUseCase
) :
    ViewModel() {
    private val tag = this::class.java.simpleName
    var user by mutableStateOf(User())
        private set
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _notesData = MutableStateFlow<List<Note>>(emptyList())
    val notesData: StateFlow<List<Note>> = _notesData

    fun fetchNotes() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {

            getMyNotesByDateUseCase.execute(
                date = getCurrentDate(),
                callback = { notes ->
                    _notesData.value = notes
                    _isLoading.value = false
                },
                onError = {
                    Log.e(tag, "fetchNotes - getMyNotesByDateUseCase")
                }
            )

            getCurrentUserUseCase.execute().onSuccess { value: User ->
                user = value
            }
        }
    }

    @Composable
    fun getWelcomeGretting(): String {
        val welcomeGretting = when (getTimeOfDay()) {
            TimeOfDay.GOOD_MORNING -> R.string.label_welcome_good_morning
            TimeOfDay.GOOD_AFTERNOON -> R.string.label_welcome_good_afternoon
            TimeOfDay.GOOD_NIGHT -> R.string.label_welcome_good_night
        }

        return "${stringResource(id = welcomeGretting)}, ${user.name.split(" ")[0]}"

    }

    @Composable
    fun getUserAvatar(): Painter {
        return if (user.photoUrl != null) {
            rememberImagePainter(data = user.photoUrl)
        } else {
            painterResource(id = R.drawable.hombre)
        }

    }

}