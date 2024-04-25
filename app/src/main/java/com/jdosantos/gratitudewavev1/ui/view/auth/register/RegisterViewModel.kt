package com.jdosantos.gratitudewavev1.ui.view.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val signUpUseCase: SignUpUseCase) : ViewModel() {
    var showAlert by mutableStateOf(false)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun register(email: String, name: String, password: String, callback: (Boolean) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {

            val user: User = User().copy(email = email, name = name)

            signUpUseCase.execute(user, password) { success ->
                callback.invoke(success)
                _isLoading.value = !success
            }
        }
    }

    fun closeAlert() {
        showAlert = false
    }
}