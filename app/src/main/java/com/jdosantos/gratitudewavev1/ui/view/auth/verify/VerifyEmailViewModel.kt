package com.jdosantos.gratitudewavev1.ui.view.auth.verify

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.data.local.CredentialStore
import com.jdosantos.gratitudewavev1.domain.handles.LocalizedMessageManager
import com.jdosantos.gratitudewavev1.domain.handles.SingleLiveEvent
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.GetCurrentUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.auth.LogoutUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.auth.ReauthenticateUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.auth.SendEmailVerificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val reauthenticateUseCase: ReauthenticateUseCase,
    private val credentialStore: CredentialStore,
    private val localizedMessageManager: LocalizedMessageManager
) : ViewModel() {

    private val _emailCurrentUser = MutableStateFlow<String>("")
    val emailCurrentUser: StateFlow<String> = _emailCurrentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

    private val _logoutSuccess = MutableStateFlow(false)
    val logoutSuccess: StateFlow<Boolean> = _logoutSuccess

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun reauthenticate() {
        viewModelScope.launch {
            credentialStore.getPassword().collect { password ->
                reauthenticateUseCase.execute(password!!)
                    .onSuccess {
                        _registerSuccess.value = true
                    }.onFailure { exception ->
                        _toastMessage.value = exception.message
                    }
            }
        }

    }

    fun resendLink() {
        viewModelScope.launch {
            sendEmailVerificationUseCase.execute()
                .onSuccess {
                    _toastMessage.value =
                        localizedMessageManager.getMessage(LocalizedMessageManager.MessageKey.RESET_SEND_EMAIL_SUCCESS)
                }
                .onFailure {
                    _toastMessage.value =
                        localizedMessageManager.getMessage(LocalizedMessageManager.MessageKey.RESET_SEND_EMAIL_ERROR)
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

    fun getCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase.execute().onSuccess { value: User ->
                _emailCurrentUser.value = value.email
            }
        }
    }
}