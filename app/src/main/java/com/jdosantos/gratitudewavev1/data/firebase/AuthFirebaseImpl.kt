package com.jdosantos.gratitudewavev1.data.firebase

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.models.UserData
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(DelicateCoroutinesApi::class)
@Singleton
internal class AuthFirebaseImpl @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {
    private val tag = this::class.java.simpleName

    private val user = MutableStateFlow<User>(User.LoggedOut)
    override fun get(): Flow<User> {
        return user
    }

    override fun getCurrentUser(callback: (UserData) -> Unit, onError: () -> Unit) {
        Log.d(tag, "getCurrentUser")
        try {
            val userLogged = auth.currentUser
            val userDataFound = UserData(
                uid = userLogged!!.uid,
                email = userLogged.email.toString(),
                name = userLogged.displayName.toString(),
                photoUrl = userLogged.photoUrl,
                provider = userLogged.providerId
            )
            callback.invoke(userDataFound)
        } catch (e: Exception) {
            onError.invoke()
            Log.e(tag, "getCurrentUser - error: ${e.message}")
        }


    }

    override suspend fun login(email: String, password: String) {

        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val isEmailVerified = auth.currentUser!!.isEmailVerified
                    GlobalScope.launch {
                        user.emit(
                            value = User.LoggedIn(
                                data = UserData(
                                    uid = it.result.user!!.uid,
                                    email = it.result.user!!.email.toString()
                                )
                            )
                        )
                    }
                } else {
                    Log.d(tag, "login - fail ${it.exception?.message}")
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "login - error: ${e.message}")
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
        callback: (isEmailVerified: Boolean) -> Unit,
        onError: () -> Unit
    ) {
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val isEmailVerified = auth.currentUser!!.isEmailVerified
                    callback.invoke(isEmailVerified)
                } else {
                    Log.d(tag, "signInWithEmailAndPassword - fail ${it.exception?.message}")
                    onError.invoke()
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "signInWithEmailAndPassword - error: ${e.message}")
            onError.invoke()

        }
    }

    override fun logout(callback: (success: Boolean) -> Unit) {
        try {
            auth.signOut()
            callback.invoke(true)
        } catch (e: Exception) {
            callback.invoke(false)
            Log.e(tag, "logout - error: ${e.message}")
        }

    }

    override suspend fun signInWithGoogle(
        credential: AuthCredential,
        callback: (UserData) -> Unit,
        onError: () -> Unit
    ) {
        try {
            auth.signInWithCredential(credential).addOnCompleteListener {

                if (it.isSuccessful) {
                    val userLogged = it.result.user

                    val userDataFound = UserData(
                        uid = userLogged!!.uid,
                        email = userLogged.email.toString(),
                        name = userLogged.displayName.toString(),
                        photoUrl = userLogged.photoUrl,
                        provider = userLogged.providerId
                    )
                    callback.invoke(userDataFound)
                } else {
                    Log.d(tag, "signInWithGoogle - fail ${it.exception?.message}")
                    onError.invoke()
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "signInWithGoogle - error: ${e.message}")
            onError.invoke()
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        callback: (success: Boolean) -> Unit
    ) {
        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    sendEmailVerification(callback)
                } else {
                    Log.d(tag, "loginGoogle - fail ${it.exception?.message}")
                    callback.invoke(false)
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "signUp - error: ${e.message}")
            callback.invoke(false)
        }
    }

    override fun sendEmailVerification(callback: (success: Boolean) -> Unit) {
        try {
            auth.currentUser?.sendEmailVerification()
                ?.addOnCompleteListener {
                    callback.invoke(it.isSuccessful)
                    if (it.isSuccessful) {
                        Log.i(tag, "sendEmailVerification - email verification sent ${auth.currentUser!!.email}")
                    } else {
                        Log.d(tag, "sendEmailVerification - fail ${it.exception?.message}")
                    }
                }
        } catch (e: Exception) {
            Log.e(tag, "sendEmailVerification - error: ${e.message}")
            callback.invoke(false)
        }
    }


}