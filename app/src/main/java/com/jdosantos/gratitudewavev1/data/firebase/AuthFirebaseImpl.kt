package com.jdosantos.gratitudewavev1.data.firebase

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.jdosantos.gratitudewavev1.domain.exceptions.AuthenticationException
import com.jdosantos.gratitudewavev1.domain.models.LoggedUser
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
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
                        Log.i(
                            tag,
                            "sendEmailVerification - email verification sent ${auth.currentUser!!.email}"
                        )
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

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val userCredential = auth.signInWithEmailAndPassword(email, password).await()
            val isEmailVerified = userCredential.user!!.isEmailVerified
            if (isEmailVerified) {
                Result.success(User(userCredential.user?.email ?: ""))
            } else {
                Result.failure(AuthenticationException.EmailNotVerifiedException())
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(AuthenticationException.InvalidCredentialsException())
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(AuthenticationException.InvalidCredentialsException())
        } catch (e: Exception) {
            Result.failure(AuthenticationException.GenericAuthenticationException())
        }
    }

    override suspend fun reauthenticate(password: String): Result<User> {
        return try {
            Log.d(tag, "reauthenticate - password: $password")
            val credential = EmailAuthProvider.getCredential(auth.currentUser!!.email!!, password)
            auth.currentUser!!.reauthenticate(credential).await()

            val userCredential = auth.currentUser
            val isEmailVerified = userCredential!!.isEmailVerified
            if (isEmailVerified) {
                Result.success(User(userCredential.email ?: ""))
            } else {
                Result.failure(AuthenticationException.EmailNotVerifiedException())
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(AuthenticationException.InvalidCredentialsException())
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(AuthenticationException.InvalidCredentialsException())
        } catch (e: Exception) {
            Log.e(tag, "reauthenticate - error: ${e.message}")
            Result.failure(AuthenticationException.GenericAuthenticationException())
        }
    }

    override fun isUserLoggedIn(): Result<Boolean> {
        val userCredential = auth.currentUser
        return if (userCredential != null) {
            val isEmailVerified = userCredential.isEmailVerified
            if (isEmailVerified) {
                Result.success(true)
            } else {
                Result.failure(AuthenticationException.EmailNotVerifiedException())
            }
        } else {
            Result.failure(AuthenticationException.UserNotLogged())
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }


    override fun getCurrentUser(): Result<User> {
        val userLogged = auth.currentUser
        return if (userLogged != null) {
            Result.success(User(
                uid = userLogged.uid,
                email = userLogged.email.toString(),
                name = userLogged.displayName.toString(),
                photoUrl = userLogged.photoUrl,
                provider = userLogged.providerId
            ))
        } else {
            Result.failure(AuthenticationException.UserNotLogged())
        }
    }

}