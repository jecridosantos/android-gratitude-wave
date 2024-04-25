package com.jdosantos.gratitudewavev1.ui.view.main.home.bycalendar

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.models.CalendarToShow
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetMyNotesByDateUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.notes.GetNoteCreationDatesByEmailUseCase
import com.jdosantos.gratitudewavev1.utils.convertMillisToDate
import com.jdosantos.gratitudewavev1.utils.getFormattedDateToSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ByCalendarViewModel @Inject constructor(
    private val getMyNotesByDateUseCase: GetMyNotesByDateUseCase,
    private val getNoteCreationDatesByEmailUseCase: GetNoteCreationDatesByEmailUseCase
) :
    ViewModel() {

    private val tag = this::class.java.simpleName

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isNavigateToDetail = MutableStateFlow(false)
    val isNavigateToDetail: StateFlow<Boolean> = _isNavigateToDetail

    private val _notesData = MutableStateFlow<List<Note>>(emptyList())
    val notesData: StateFlow<List<Note>> = _notesData

    private val _calendar = MutableStateFlow<List<CalendarToShow>>(emptyList())
    val calendar: StateFlow<List<CalendarToShow>> = _calendar

    private val _selectedDate = mutableStateOf(Date())
    val selectedDate: State<Date> = _selectedDate

    fun clean() {
        _selectedDate.value = Date()
        _notesData.value = emptyList()
    }

    init {
        viewModelScope.launch {
            getNoteCreationDatesByEmailUseCase.generateCalendar(callback = { months ->
                _calendar.value = months
            }, onError = {
                Log.e(tag, "init - getNoteCreationDatesByEmailUseCase")
            })
        }
    }

    private fun fetchNotes() {
        val searchDate = getFormattedDateToSearch(_selectedDate.value)
        _isLoading.value = true
        _notesData.value = emptyList()

        getMyNotesByDateUseCase.execute(searchDate, callback = { notes ->
            _notesData.value = notes
            _isLoading.value = false
        }, onError = {
            Log.e(tag, "fetchNotes - getMyNotesByDateUseCase")
        })
    }

    fun selectDate(date: Date) {
        _selectedDate.value = date
        fetchNotes()
    }

    fun searchTodayNotes() {
        _selectedDate.value = Date()
        fetchNotes()
    }

    fun navigateToSelectedPicker(millis: Long) {
        _selectedDate.value = convertMillisToDate(millis)!!
        fetchNotes()
    }

    fun navigateToDetail(value: Boolean) {
        _isNavigateToDetail.value = value
    }


}