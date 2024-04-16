package com.jdosantos.gratitudewavev1.ui.view.auth.verify

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.jdosantos.gratitudewavev1.app.store.CredentialStore
import com.jdosantos.gratitudewavev1.app.usecase.LogoutUseCase
import com.jdosantos.gratitudewavev1.app.usecase.SendEmailVerificationUseCase
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

    fun resendLink(onSuccess: () -> Unit, onError: () -> Unit) {
        sendEmailVerificationUseCase.execute(onSuccess, onError)
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase.execute(onSuccess) {
                Log.d("Error logout", "Error logout")
            }
        }
    }

}