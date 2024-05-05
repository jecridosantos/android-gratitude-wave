package com.jdosantos.gratitudewavev1.ui.view.main.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.models.UserData
import com.jdosantos.gratitudewavev1.domain.usecase.auth.GetCurrentUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val tag = this::class.java.simpleName
    var userData by mutableStateOf(UserData())
        private set
    fun getCurrentUser() {
        getCurrentUserUseCase.execute({
            userData = it
        }) {
            Log.e(tag, "getCurrentUser - getCurrentUserUseCase")
        }
    }

    fun logout(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            logoutUseCase.execute(callback)
        }
    }
}