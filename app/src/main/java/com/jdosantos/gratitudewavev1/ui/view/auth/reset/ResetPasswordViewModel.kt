package com.jdosantos.gratitudewavev1.ui.view.auth.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.handles.SingleLiveEvent
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.ResetPasswordUseCase
import com.jdosantos.gratitudewavev1.utils.isEmailValid
import com.jdosantos.gratitudewavev1.utils.isNameValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _isButtonEnabled = MutableStateFlow(false)
    val isButtonEnabled: StateFlow<Boolean> = _isButtonEnabled
    fun reset() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = resetPasswordUseCase.execute(_email.value)

            result.onSuccess {
                _registerSuccess.value = true
            }.onFailure { exception ->
                _toastMessage.value = exception.message
            }

            _isLoading.value = false
        }

    }

    fun hideAlert() {
        _registerSuccess.value = false
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
        _isButtonEnabled.value = isEmailValid(_email.value)
    }

}