package com.jdosantos.gratitudewavev1.ui.view.auth.register

import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val signUpUseCase: SignUpUseCase) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun register(email: String, name: String, password: String): Result<Boolean> {
        _isLoading.value = true
        val user: User = User().copy(email = email, name = name)
        val result = signUpUseCase.execute(user, password)
        _isLoading.value = false
        return result

    }

}