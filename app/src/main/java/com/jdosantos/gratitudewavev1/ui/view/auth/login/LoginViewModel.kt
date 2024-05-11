package com.jdosantos.gratitudewavev1.ui.view.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    var showAlert by mutableStateOf(false)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val isLoggedIn = authRepository.get()
        .map { it is User.LoggedIn }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (isEmailVerified: Boolean) -> Unit
    ) {
        viewModelScope.launch {

            authRepository.signInWithEmailAndPassword(
                email,
                password,
                callback = {
                    onSuccess(it)
                },
                onError = { showAlert = true }
            )
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password)
        }
    }


    fun signInWithGoogle(credential: AuthCredential, callback: (Boolean) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            authRepository.signInWithGoogle(
                credential = credential,
                callback = { userLogged ->
                    userRepository.getUserByUid(
                        uid = userLogged.uid,
                        callback = { user ->
                            if (user!!.uid == userLogged.uid) {
                                callback.invoke(true)
                            } else {
                                userRepository.saveUser(userLogged, callback)
                            }
                        },
                        onError = { callback.invoke(false) }
                    )
                },
                onError = {
                    callback.invoke(false)
                })
        }
    }

    fun closeAlert() {
        showAlert = false
    }

}