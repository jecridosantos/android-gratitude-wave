package com.jdosantos.gratitudewavev1.ui.view.main.home.bycalendar

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.core.common.util.convertMillisToDate
import com.jdosantos.gratitudewavev1.core.common.util.getFormattedDateToSearch
import com.jdosantos.gratitudewavev1.app.model.CalendarToShow
import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.usecase.notes.GetMyNotesByDateUseCase
import com.jdosantos.gratitudewavev1.app.usecase.notes.GetNoteCreationDatesByEmailUseCase
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
           // searchTodayNotes()
            getNoteCreationDatesByEmailUseCase.generateCalendar { months ->
                _calendar.value = months

            }
        }
    }

    private fun fetchNotes() {
        val searchDate = getFormattedDateToSearch(_selectedDate.value)
        _isLoading.value = true
        _notesData.value = emptyList()

        getMyNotesByDateUseCase.execute(searchDate) { notes ->

            _notesData.value = notes
            _isLoading.value = false
        }

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