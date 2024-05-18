package com.jdosantos.gratitudewavev1.ui.view.auth.reset

import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
): ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    suspend fun reset(email: String): Result<Boolean> {
        _isLoading.value = true
        val result = resetPasswordUseCase.execute(email)
        _isLoading.value = false
        return result
    }

}