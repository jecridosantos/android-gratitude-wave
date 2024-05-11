package com.jdosantos.gratitudewavev1.ui.view.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult

    fun signInWithEmailAndPassword(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _loginResult.value = loginUseCase.login(email, password)
            _isLoading.value = false
        }
    }

    fun signInWithGoogle(credential: AuthCredential, callback: (Boolean) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            _loginResult.value = loginUseCase.signInWithGoogle(credential)
            _isLoading.value = false
        }
    }

}