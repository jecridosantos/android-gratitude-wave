package com.jdosantos.gratitudewavev1.ui.view.main.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.app.model.User
import com.jdosantos.gratitudewavev1.app.usecase.GetCurrentUserUseCase
import com.jdosantos.gratitudewavev1.app.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    var user by mutableStateOf(User())
        private set
    fun getCuurrentUser() {
        getCurrentUserUseCase.execute({
            user = it
        }) {}
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase.execute(onSuccess) {
                Log.d("Error logout", "Error logout")
            }
        }
    }
}