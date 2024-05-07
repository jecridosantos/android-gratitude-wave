package com.jdosantos.gratitudewavev1.ui.view.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _isAuthenticated = MutableLiveData<Result<Boolean>>()
    val isAuthenticated: LiveData<Result<Boolean>> = _isAuthenticated

    fun checkSession() {
        viewModelScope.launch {
            _isAuthenticated.value = loginUseCase.isUserLoggedIn()
        }
    }
}