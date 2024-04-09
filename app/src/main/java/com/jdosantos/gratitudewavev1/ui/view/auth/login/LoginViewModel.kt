package com.jdosantos.gratitudewavev1.ui.view.auth.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.jdosantos.gratitudewavev1.app.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    var showAlert by mutableStateOf(false)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    fun login(email: String, password: String, onSuccess: (isEmailVerified : Boolean) -> Unit) {
        viewModelScope.launch {

            loginUseCase.execute(email, password, onSuccess) {
                showAlert = true
                Log.d("Error en login", "Credenciales incorrectas")
            }
        }
    }

    fun signInWithGoogle(credential: AuthCredential, onSuccess: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            Log.d("Login google", "3")
            loginUseCase.signInWithGoogle(credential, {
                onSuccess()
                _isLoading.value = false
            }) {
                showAlert = true
                _isLoading.value = false
                Log.d("Error en login", "Credenciales incorrectas")
            }
        }
    }

    fun isLogged(): Boolean {
        val isLoggedUser = loginUseCase.isLogged()
        Log.d("LoginViewModel", "isLoggedUser: $isLoggedUser")
        return isLoggedUser
    }

    fun closeAlert() {
        showAlert = false
    }

}