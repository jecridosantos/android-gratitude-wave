package com.jdosantos.gratitudewavev1.ui.view.main.home

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
import com.jdosantos.gratitudewavev1.core.common.util.getCurrentDate
import com.jdosantos.gratitudewavev1.core.common.util.getTimeOfDay
import com.jdosantos.gratitudewavev1.app.enums.TimeOfDay
import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.model.User
import com.jdosantos.gratitudewavev1.app.usecase.GetCurrentUserUseCase
import com.jdosantos.gratitudewavev1.app.usecase.notes.GetMyNotesByDateUseCase
import com.jdosantos.gratitudewavev1.app.usecase.notes.GetNotesByCurrentUserUseCase
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

    var user by mutableStateOf(User())
        private set
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

/*
    var showMessageFirstNote by mutableStateOf(true)

    var noNoteOfTheDay by mutableStateOf(false)*/

    private val _notesData = MutableStateFlow<List<Note>>(emptyList())
    val notesData: StateFlow<List<Note>> = _notesData

    fun fetchNotes() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            getMyNotesByDateUseCase.execute(getCurrentDate()) { notes ->
                _notesData.value = notes
                /*
            noNoteOfTheDay = notes[0].date != getCurrentDate()*/

                _isLoading.value = false
            }

            getCurrentUserUseCase.execute({
                user = it
            }) {}
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
/*

    fun hideIsNoteOfTheDay() {
        noNoteOfTheDay = false
    }

    fun saveFlagShowMessage(context: Context, value: Boolean) {
  */
/*      viewModelScope.launch(Dispatchers.IO) {
            FlagStore(context).saveFirstNote(if (value) 1 else 0)
        }*//*

    }
*/

}