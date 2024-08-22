package com.jdosantos.gratitudewavev1.ui.view.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.jdosantos.gratitudewavev1.domain.exceptions.AuthenticationException
import com.jdosantos.gratitudewavev1.domain.handles.SingleLiveEvent
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.LoginUseCase
import com.jdosantos.gratitudewavev1.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _unverifiedEmail = MutableStateFlow(false)
    val unverifiedEmail: StateFlow<Boolean> = _unverifiedEmail

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> = _toastMessage

    fun signInWithEmailAndPassword(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = loginUseCase.login(email, password)
            _isLoading.value = false
            handleResult(result)
        }
    }

    fun signInWithGoogle(credential: AuthCredential) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = loginUseCase.signInWithGoogle(credential)
            _isLoading.value = false
            handleResult(result)
        }
    }

    private fun handleResult(result: Result<User>) {
        result.onSuccess {
            _loginSuccess.value = true
        }.onFailure { exception ->
            _toastMessage.value = when (exception) {
                is AuthenticationException.EmailNotVerifiedException -> {
                    _unverifiedEmail.value = true
                    exception.message
                }
                else -> exception.message
            }
        }
    }

}