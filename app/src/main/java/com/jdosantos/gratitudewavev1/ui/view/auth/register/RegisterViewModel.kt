package com.jdosantos.gratitudewavev1.ui.view.auth.register

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.app.model.User
import com.jdosantos.gratitudewavev1.app.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(val signUpUseCase: SignUpUseCase) : ViewModel() {
    var showAlert by mutableStateOf(false)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun register(email: String, name: String, password: String, onSuccess: () -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {

            val user: User = User().copy(email = email, name = name)

            signUpUseCase.execute(user, password, {
                _isLoading.value = false
                onSuccess()
            }) {
                showAlert = true
                _isLoading.value = false
                Log.d("Error en register", "Error registro")
            }
        }
    }

    fun closeAlert() {
        showAlert = false
    }
}