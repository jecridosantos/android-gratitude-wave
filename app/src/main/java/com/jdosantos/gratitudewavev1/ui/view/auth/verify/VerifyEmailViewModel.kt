package com.jdosantos.gratitudewavev1.ui.view.auth.verify

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.jdosantos.gratitudewavev1.data.local.CredentialStore
import com.jdosantos.gratitudewavev1.domain.usecase.auth.LogoutUseCase
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
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    suspend fun init(context: Context, navController: NavController) {
        val dataStore = CredentialStore(context)
        dataStore.getPassword().collect { value ->
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val credential = EmailAuthProvider
                .getCredential(firebaseUser!!.email!!, value!!)

            firebaseUser.reauthenticate(credential).addOnCompleteListener {
                if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                    navController.navigate("ContainerView") {
                        popUpTo("SplashView") { inclusive = true }
                    }
                }
            }
        }
    }

    fun resendLink(callback: (success: Boolean) -> Unit) {
        sendEmailVerificationUseCase.execute(callback)
    }

    fun logout(callback: (success: Boolean) -> Unit) {
        viewModelScope.launch {
            logoutUseCase.execute(callback)
        }
    }

}