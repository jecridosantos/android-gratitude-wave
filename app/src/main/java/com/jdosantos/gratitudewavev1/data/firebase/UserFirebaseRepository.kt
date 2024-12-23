package com.jdosantos.gratitudewavev1.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdosantos.gratitudewavev1.domain.exceptions.AuthenticationException
import com.jdosantos.gratitudewavev1.domain.exceptions.GenericException
import com.jdosantos.gratitudewavev1.domain.exceptions.UserException
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class UserFirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth?,
    db: FirebaseFirestore
) : UserRepository {
    private val tag = this::class.java.simpleName
    private val uid: String?
        get() = auth?.currentUser?.uid

    private val collection: CollectionReference = db.collection("Users")

    override suspend fun saveUser(user: User): Result<User> {
        Log.d(tag, "saveUser")
        return suspendCancellableCoroutine { continuation ->
            try {
                val newNote = hashMapOf(
                    "uid" to uid,
                    "email" to user.email,
                    "name" to user.name,
                    "photoUrl" to user.photoUrl,
                    "provider" to user.provider,
                    "createAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                )
                collection.add(newNote)
                    .addOnSuccessListener { documentReference ->
                        val savedUser = user.copy(id = documentReference.id)
                        continuation.resume(Result.success(savedUser))
                    }
                    .addOnFailureListener { exception ->
                        Log.e(tag, "saveUser - error: ${exception.message}")
                        continuation.resume(Result.failure(UserException.UserSaveError()))
                    }
            } catch (e: Exception) {
                Log.e(tag, "saveUser - error: ${e.message}")
                continuation.resume(Result.failure(UserException.UserSaveError()))
            }
        }
    }

    override suspend fun updateName(userId: String, name: String): Result<Boolean> {
        Log.d(tag, "saveUser")
        return suspendCancellableCoroutine { continuation ->
            try {
                val updateName = hashMapOf(
                    "name" to name,
                    "updateAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                )
                collection.document(userId)
                    .update(updateName as Map<String, Any>)
                    .addOnSuccessListener { _ ->
                        continuation.resume(Result.success(true))
                    }
                    .addOnFailureListener { exception ->
                        Log.e(tag, "updateName - error: ${exception.message}")
                        continuation.resume(Result.failure(UserException.UserSaveError()))
                        continuation.resume(Result.success(false))
                    }
            } catch (e: Exception) {
                Log.e(tag, "updateName - error: ${e.message}")
                continuation.resume(Result.failure(UserException.UserSaveError()))
            }
        }
    }

    override suspend fun getUserByUid(
        uid: String

    ): Result<User> {
        Log.d(tag, "getUserByUid")

        return suspendCancellableCoroutine { continuation ->
            collection
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    var user: User? = null
                    if (!querySnapshot.isEmpty) {
                        for (document in querySnapshot) {
                            val myDocument =
                                document.toObject(User::class.java).copy(id = document.id)
                            user = myDocument
                        }
                        user?.let {
                            continuation.resume(Result.success(it))
                        }
                    } else {
                        continuation.resume(Result.failure(UserException.UserNotFound()))
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(tag, "getUserByUid - error: ${e.message}")
                    continuation.resume(Result.failure(GenericException.ClientException()))
                }
        }
    }

    override suspend fun emailExists(email: String): Result<Boolean> {
        return suspendCancellableCoroutine { continuation ->
            collection.whereEqualTo("email", email).get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {

                        Log.d(
                            tag,
                            "emailExists - No existe un usuario con el correo electrónico $email."
                        )
                        continuation.resume(Result.failure(AuthenticationException.EmailNotFoundException()))
                    } else {
                        Log.d(
                            tag,
                            "emailExists - Existe un usuario con el correo electrónico $email."
                        )
                        continuation.resume(Result.success(true))
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(tag, "emailExists - error: ${exception.message}")
                }
        }
    }
}