package com.jdosantos.gratitudewavev1.ui.view.main.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.GetCurrentUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val tag = this::class.java.simpleName
    var user by mutableStateOf(User())
        private set

    fun getCuurrentUser() {
        getCurrentUserUseCase.execute().onSuccess { value: User ->
            user = value
        }
    }

    suspend fun logout(): Result<Boolean> {

        return logoutUseCase.execute()

    }

}