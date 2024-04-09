package com.jdosantos.gratitudewavev1.app.repository.impl

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.jdosantos.gratitudewavev1.app.model.LoggedUser
import com.jdosantos.gratitudewavev1.app.model.User
import com.jdosantos.gratitudewavev1.app.repository.AuthRepository
import javax.inject.Inject

class AuthFirebaseImpl @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {
    override fun getCurrentUser(onSuccess: (User) -> Unit, onError: () -> Unit) {
        try {
            val userLogged = auth.currentUser
            val userFound = User(
                uid = userLogged!!.uid,
                email = userLogged.email.toString(),
                name = userLogged.displayName.toString(),
                photoUrl = userLogged.photoUrl,
                provider = userLogged.providerId
            )
            onSuccess(userFound)
        } catch (e: Exception) {
            onError()
        }


    }

    override suspend fun login(
        email: String,
        password: String,
        onSuccess: (isEmailVerified: Boolean) -> Unit,
        onError: () -> Unit
    ) {
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val isEmailVerified = auth.currentUser!!.isEmailVerified
                    onSuccess(isEmailVerified)
                } else {
                    onError()
                }
            }
        } catch (e: Exception) {
            onError()
        }
    }

    override fun logout(onSuccess: () -> Unit, onError: () -> Unit) {
        try {
            auth.signOut()
            onSuccess()
        } catch (e: Exception) {
            onError()
        }

    }

    override fun loggedUser(): LoggedUser {
        return try {
            val uid = auth.currentUser?.uid
            val email = auth.currentUser?.email
            Log.d("AuthFirebaseImpl", "uid: $uid")
            if (uid!!.isNotEmpty()) {
                LoggedUser(uid.toString(), email.toString())
            } else {
                LoggedUser()
            }
        } catch (e: Exception) {
            LoggedUser()
        }
    }

    override suspend fun loginGoogle(
        credential: AuthCredential,
        onSuccess: (User) -> Unit,
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
                    onSuccess(userFound)
                } else {
                    Log.d("Error en login google", "Credenciales incorrectas")
                    onError()
                }
            }
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("Error en login google", it) }
            onError()
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {

                    sendEmailVerification(onSuccess, onError)
                } else {
                    onError()
                }
            }
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("signUp", it) }
            onError()
        }
    }

    override fun sendEmailVerification(onSuccess: () -> Unit, onError: () -> Unit) {
        try {
            auth.currentUser?.sendEmailVerification()
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(
                            "Email verify",
                            "Verificacion de email enviada ${auth.currentUser!!.email}"
                        )
                        onSuccess()
                    } else {
                        Log.d("Email verify", "Error verificacion de email")
                    }

                }
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("signUp", it) }
            onError()
        }
    }


}