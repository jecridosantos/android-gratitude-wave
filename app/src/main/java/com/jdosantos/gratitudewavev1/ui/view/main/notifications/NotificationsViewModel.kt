package com.jdosantos.gratitudewavev1.ui.view.main.notifications

import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.domain.models.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _data = MutableStateFlow<List<Notification>>(emptyList())
    val data: StateFlow<List<Notification>> = _data


}