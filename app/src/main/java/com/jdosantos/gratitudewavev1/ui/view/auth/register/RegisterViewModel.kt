package com.jdosantos.gratitudewavev1.ui.view.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.data.local.CredentialStore
import com.jdosantos.gratitudewavev1.domain.handles.SingleLiveEvent
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.SignUpUseCase
import com.jdosantos.gratitudewavev1.utils.isEmailValid
import com.jdosantos.gratitudewavev1.utils.isNameValid
import com.jdosantos.gratitudewavev1.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val credentialStore: CredentialStore
) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isButtonEnabled = MutableStateFlow(false)
    val isButtonEnabled: StateFlow<Boolean> = _isButtonEnabled

    private val _passwordValidation = MutableStateFlow(false)
    fun updateName(newName: String) {
        _name.value = newName
        checkFormValidity()
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
        checkFormValidity()
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
        Log.d("PASSWORD", _password.value)
        _passwordValidation.value = validatePassword(newPassword, _name.value, _email.value)
        checkFormValidity()
    }



    fun register() {
        _isLoading.value = true
        val user: User = User().copy(email = _email.value.trim(), name = _name.value)

        viewModelScope.launch {
            val result = signUpUseCase.execute(user, _password.value)

            _isLoading.value = false

            result.onSuccess {
                credentialStore.savePassword(_password.value)
                _registerSuccess.value = true
            }.onFailure { exception ->
                _toastMessage.value = exception.message
            }
        }
    }

    private fun checkFormValidity() {
        val name = isNameValid(_name.value)
        val email = isEmailValid(_email.value)
        val pass = _passwordValidation.value
        _isButtonEnabled.value = name && email && pass
    }

}