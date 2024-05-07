package com.jdosantos.gratitudewavev1.ui.view.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var showAlert by mutableStateOf(false)
    private val _isLoading = MutableStateFlow(false)

    private val _loginResult = MutableLiveData<Result<User>>()
    val loginResult: LiveData<Result<User>> = _loginResult

    val isLoading: StateFlow<Boolean> = _isLoading

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = loginUseCase.login(email, password)
        }
    }

    fun signInWithGoogle(credential: AuthCredential, callback: (Boolean) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            loginUseCase.signInWithGoogle(credential) { success ->
                callback.invoke(success)
                _isLoading.value = !success
            }
        }
    }

    fun closeAlert() {
        showAlert = false
    }

}