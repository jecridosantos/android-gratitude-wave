package com.jdosantos.gratitudewavev1.ui.view.splash

import androidx.lifecycle.ViewModel
import com.jdosantos.gratitudewavev1.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authenticateUseCase: LoginUseCase
) : ViewModel() {

/*    private val _isAuthenticated = MutableStateFlow<UserAuthState>(value = UserAuthState.UNKNOWN)
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
    }*/

}