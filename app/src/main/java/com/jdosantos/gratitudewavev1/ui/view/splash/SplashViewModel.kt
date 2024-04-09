package com.jdosantos.gratitudewavev1.ui.view.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdosantos.gratitudewavev1.app.enums.UserAuthState
import com.jdosantos.gratitudewavev1.app.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authenticateUseCase: LoginUseCase
) : ViewModel() {

    private val _isAuthenticated = MutableStateFlow<UserAuthState>(value = UserAuthState.UNKNOWN)
    val isAuthenticated: StateFlow<UserAuthState> = _isAuthenticated.asStateFlow()

    fun onEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.CheckAuthentication -> {
                viewModelScope.launch {
                    val result = authenticateUseCase.isLogged()
                    when (result) {
                        true -> {
                            _isAuthenticated.emit(UserAuthState.AUTHENTICATED)
                        }

                        false -> {
                            _isAuthenticated.emit(UserAuthState.UNAUTHENTICATED)
                        }
                    }
                }
            }
        }
    }

}