package com.jdosantos.gratitudewavev1.data.firebase

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.jdosantos.gratitudewavev1.domain.models.LoggedUser
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import javax.inject.Inject

class AuthFirebaseImpl @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {
    private val tag = this::class.java.simpleName
    override fun getCurrentUser(callback: (User) -> Unit, onError: () -> Unit) {
        Log.d(tag, "getCurrentUser")
        try {
            val userLogged = auth.currentUser
            val userFound = User(
                uid = userLogged!!.uid,
                email = userLogged.email.toString(),
                name = userLogged.displayName.toString(),
                photoUrl = userLogged.photoUrl,
                provider = userLogged.providerId
            )
            callback.invoke(userFound)
        } catch (e: Exception) {
            onError.invoke()
            Log.e(tag, "getCurrentUser - error: ${e.message}")
        }


    }

    override suspend fun login(
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
                    Log.d(tag, "login - fail ${it.exception?.message}")
                    onError.invoke()
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "login - error: ${e.message}")
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

    override fun loggedUser(): LoggedUser {
        return try {
            val uid = auth.currentUser?.uid
            val email = auth.currentUser?.email
            Log.d(tag, "loggedUser - user logged with uid: $uid")
            if (uid!!.isNotEmpty()) {
                LoggedUser(uid.toString(), email.toString())
            } else {
                LoggedUser()
            }
        } catch (e: Exception) {
            Log.e(tag, "loggedUser - error: ${e.message}")
            LoggedUser()
        }
    }

    override suspend fun loginGoogle(
        credential: AuthCredential,
        callback: (User) -> Unit,
        onError: () -> Unit
    ) {
        try {
            auth.signInWithCredential(credential).addOnCompleteListener {

                if (it.isSuccessful) {
                    val userLogged = it.result.user

                    val userFound = User(
                        uid = userLogged!!.uid,
                        email = userLogged.email.toString(),
                        name = userLogged.displayName.toString(),
                        photoUrl = userLogged.photoUrl,
                        provider = userLogged.providerId
                    )
                    callback.invoke(userFound)
                } else {
                    Log.d(tag, "loginGoogle - fail ${it.exception?.message}")
                    onError.invoke()
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "loginGoogle - error: ${e.message}")
            onError.invoke()
        }
    }

    override suspend fun signUp(
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
                    if (it.isSuccessful) {
                        Log.i(tag, "sendEmailVerification - email verification sent ${auth.currentUser!!.email}")
                        callback.invoke(true)
                    } else {
                        Log.d(tag, "sendEmailVerification - fail ${it.exception?.message}")
                        callback.invoke(false)
                    }
                }
        } catch (e: Exception) {
            Log.e(tag, "sendEmailVerification - error: ${e.message}")
            callback.invoke(false)
        }
    }


}