package com.jdosantos.gratitudewavev1.ui.view.main.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.handles.SingleLiveEvent
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.GetCurrentUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.auth.LogoutUseCase
import com.jdosantos.gratitudewavev1.utils.constants.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val tag = this::class.java.simpleName
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    private val _userAvatar = MutableStateFlow(Constants.LOGO_YUSPA_URL)
    val userAvatar: StateFlow<String?> = _userAvatar

    private val _logoutSuccess = MutableStateFlow(false)
    val logoutSuccess: StateFlow<Boolean> = _logoutSuccess

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> = _toastMessage
    fun getCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase.execute().onSuccess { user: User ->
                if (!user.photoUrl.isNullOrEmpty()) {
                    _userAvatar.value = user.photoUrl
                }
                _userName.value = user.name
                _userEmail.value = user.email
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            logoutUseCase.execute()
                .onSuccess {
                    _logoutSuccess.value = true
                }.onFailure { exception ->
                    _toastMessage.value = exception.message
                }
        }
    }

}