package com.jdosantos.gratitudewavev1.ui.view.auth.verify

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.data.local.CredentialStore
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.usecase.auth.GetCurrentUserUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.auth.LogoutUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.auth.ReauthenticateUseCase
import com.jdosantos.gratitudewavev1.domain.usecase.auth.SendEmailVerificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val reauthenticateUseCase: ReauthenticateUseCase
) : ViewModel() {


    private val _reauthenticateResult = MutableLiveData<Result<User>>()
    val reauthenticateResult: LiveData<Result<User>> = _reauthenticateResult


    private val _emailCurrentUser = MutableStateFlow<String>("")
    val emailCurrentUser: StateFlow<String> = _emailCurrentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun reauthenticate(context: Context) {
        val dataStore = CredentialStore(context)
        dataStore.getPassword().collect { password ->
            _reauthenticateResult.value = reauthenticateUseCase.execute(password!!)
        }
    }

    suspend fun resendLink(): Result<Boolean> {
        return sendEmailVerificationUseCase.execute()
    }

    suspend fun logout(): Result<Boolean> {
        return logoutUseCase.execute()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase.execute().onSuccess { value: User ->
                _emailCurrentUser.value = value.email
            }
        }
    }
}