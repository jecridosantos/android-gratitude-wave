package com.jdosantos.gratitudewavev1.ui.view.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.data.local.PreferenceManager
import com.jdosantos.gratitudewavev1.domain.enums.TimeOfDay
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.GetCurrentUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetMyNotesByDateUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetNotesByCurrentUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.userPreferences.GetUserPreferencesUseCase
import com.jdosantos.gratitudewavev1.domain.handles.LocalizedMessageManager
import com.jdosantos.gratitudewavev1.utils.constants.Constants.Companion.LOGO_YUSPA_URL
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
    private val getNotesByCurrentUserUseCase: GetNotesByCurrentUserUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val preferenceManager: PreferenceManager,
    private val localizedMessageManager: LocalizedMessageManager
) :
    ViewModel() {
    private val tag = this::class.java.simpleName

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isFetch = MutableStateFlow(false)
    val isFetch: StateFlow<Boolean> = _isFetch

    private val _thereAreNotesToday = MutableStateFlow(false)
    val thereAreNotesToday: StateFlow<Boolean> = _thereAreNotesToday

    private val _notesData = MutableStateFlow<List<Note>>(emptyList())
    val notesData: StateFlow<List<Note>> = _notesData

    private val _showOnboarding = MutableStateFlow(false)
    val showOnboarding: StateFlow<Boolean> = _showOnboarding

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _userAvatar = MutableStateFlow(LOGO_YUSPA_URL)
    val userAvatar: StateFlow<String?> = _userAvatar

    init {
        val shouldShowOnboarding = preferenceManager.shouldShowOnboarding();
        if (shouldShowOnboarding) {
            viewModelScope.launch {
                getUserPreferencesUseCase.invoke()
                    .onFailure {
                        Log.e(tag, "getUserPreferences - getUserPreferencesUseCase")
                        setShowOnboarding(true)
                    }
            }
        }

    }

    fun setShowOnboarding(show: Boolean) {
        _showOnboarding.value = show
    }

    fun fetchNotes() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {

            getMyNotesByDateUseCase.execute(
                date = getCurrentDate(),
                callback = { notes ->
                    _thereAreNotesToday.value = notes.isNotEmpty()
                },
                onError = {
                    Log.e(tag, "fetchNotes - getMyNotesByDateUseCase")
                }
            )

            getNotesByCurrentUserUseCase.execute(
                limit = 10,
                callback = { notes ->
                    _notesData.value = notes
                    _isLoading.value = false
                    _isFetch.value = true
                },
                onError = {
                    Log.e(tag, "fetchNotes - getNotesByCurrentUserUseCase")
                }
            )

            getCurrentUserUseCase.execute().onSuccess { user: User ->
                val welcomeGretting = when (getTimeOfDay()) {
                    TimeOfDay.GOOD_MORNING -> localizedMessageManager.getMessage(
                        LocalizedMessageManager.MessageKey.WELCOME_GOOD_MORNING
                    )

                    TimeOfDay.GOOD_AFTERNOON -> localizedMessageManager.getMessage(
                        LocalizedMessageManager.MessageKey.WELCOME_GOOD_AFTERNOON
                    )

                    TimeOfDay.GOOD_NIGHT -> localizedMessageManager.getMessage(
                        LocalizedMessageManager.MessageKey.WELCOME_GOOD_NIGHT
                    )
                }

                if (!user.photoUrl.isNullOrEmpty()) {
                    _userAvatar.value = user.photoUrl
                }
                _userName.value = "$welcomeGretting, ${user.name}"
            }
        }
    }


}