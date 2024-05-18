package com.jdosantos.gratitudewavev1.data.firebase

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.jdosantos.gratitudewavev1.domain.exceptions.AuthenticationException
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthFirebaseRepository @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {
    private val tag = this::class.java.simpleName

    override suspend fun loginGoogle(
        credential: AuthCredential
    ): Result<User> {

        return try {
            auth.signInWithCredential(credential).await()
            val userLogged = auth.currentUser!!
            Result.success(
                User(
                    uid = userLogged.uid,
                    email = userLogged.email.toString(),
                    name = userLogged.displayName.toString(),
                    photoUrl = userLogged.photoUrl,
                    provider = userLogged.providerId
                )
            )
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(AuthenticationException.InvalidCredentialsException())
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(AuthenticationException.InvalidCredentialsException())
        } catch (e: Exception) {
            Result.failure(AuthenticationException.GenericAuthenticationException())
        }
    }


    override suspend fun sendEmailVerification(): Result<Boolean> {
        return try {
            auth.currentUser?.sendEmailVerification()!!.await()
            Log.i(
                tag,
                "sendEmailVerification - email verification sent ${auth.currentUser!!.email}"
            )
            Result.success(true)
        } catch (e: Exception) {
            Log.e(tag, "sendEmailVerification - error: ${e.message}")
            Result.success(false)
        }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Result<User> {
        return try {
            val userCredential = auth.signInWithEmailAndPassword(email, password).await()
            val isEmailVerified = userCredential.user!!.isEmailVerified
            if (isEmailVerified) {

                val userLogged = auth.currentUser!!
                Result.success(
                    User(
                        uid = userLogged.uid,
                        email = userLogged.email.toString(),
                        name = userLogged.displayName.toString(),
                        photoUrl = userLogged.photoUrl,
                        provider = userLogged.providerId
                    )
                )
            } else {
                Result.failure(AuthenticationException.EmailNotVerifiedException())
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            Log.e(tag, "signInWithEmailAndPassword - error: ${e.message}")
            Result.failure(AuthenticationException.InvalidCredentialsException())
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e(tag, "signInWithEmailAndPassword - error: ${e.message}")
            Result.failure(AuthenticationException.InvalidCredentialsException())
        } catch (e: Exception) {
            Log.e(tag, "signInWithEmailAndPassword - error: ${e.message}")
            Result.failure(AuthenticationException.GenericAuthenticationException())
        }
    }

    override suspend fun register(email: String, password: String): Result<User> {
        return try {

            auth.createUserWithEmailAndPassword(email, password).await()

            return sendEmailVerification().map { success ->
                return if (success) {
                    val userLogged = auth.currentUser!!
                    return Result.success(
                        User(
                            uid = userLogged.uid,
                            email = userLogged.email.toString(),
                            name = userLogged.displayName.toString(),
                            photoUrl = userLogged.photoUrl,
                            provider = userLogged.providerId
                        )
                    )
                } else {
                    Result.failure(AuthenticationException.InvalidCredentialsException())
                }
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(AuthenticationException.AuthUserCollisionException())
        } catch (e: Exception) {
            Log.e(tag, "register - error: ${e.message}")
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

    override suspend fun signOut(): Result<Boolean> {
        return try {
            auth.signOut()
            Result.success(true)
        } catch (e: Exception) {
            Log.e(tag, "logout - error: ${e.message}")
            Result.success(false)
        }
    }

    override suspend fun resetPassword(email: String): Result<Boolean> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e(tag, "resetPassword - error: ${e.message}")
            Result.success(false)
        }
    }


    override fun getCurrentUser(): Result<User> {
        val userLogged = auth.currentUser
        return if (userLogged != null) {
            Result.success(
                User(
                    uid = userLogged.uid,
                    email = userLogged.email.toString(),
                    name = userLogged.displayName.toString(),
                    photoUrl = userLogged.photoUrl,
                    provider = userLogged.providerId
                )
            )
        } else {
            Result.failure(AuthenticationException.UserNotLogged())
        }
    }

}