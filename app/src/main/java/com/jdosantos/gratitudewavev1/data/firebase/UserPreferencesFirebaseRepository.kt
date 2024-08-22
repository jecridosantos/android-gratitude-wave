package com.jdosantos.gratitudewavev1.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdosantos.gratitudewavev1.domain.exceptions.GenericException
import com.jdosantos.gratitudewavev1.domain.exceptions.UserException
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.models.UserPreferences
import com.jdosantos.gratitudewavev1.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class UserPreferencesFirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth?,
    db: FirebaseFirestore
) : UserPreferencesRepository {

    private val tag = this::class.java.simpleName
    private val uid: String?
        get() = auth?.currentUser?.uid

    private val collection: CollectionReference = db.collection("UserPreferences")
    override suspend fun saveUserPreferences(userPreferences: UserPreferences): Result<UserPreferences> {
        Log.d(tag, "saveUserPreferences")
        return suspendCancellableCoroutine { continuation ->
            try {
                val newUserPrefences = hashMapOf(
                    "uid" to uid,
                    "name" to userPreferences.name,
                    "age" to userPreferences.age,
                    "country" to userPreferences.country,
                    "interests" to userPreferences.interests,
                    "profession" to userPreferences.profession,
                    "relationship" to userPreferences.relationship,
                    "selectedInterests" to userPreferences.selectedInterests,
                    "selectedRelationships" to userPreferences.selectedRelationships,
                    "about" to userPreferences.about,
                    "skip" to userPreferences.skip,
                )

                collection.add(newUserPrefences)
                    .addOnSuccessListener { documentReference ->
                        val savedUserPreferences = userPreferences.copy(id = documentReference.id)
                        continuation.resume(Result.success(savedUserPreferences))
                    }
                    .addOnFailureListener { exception ->
                        Log.e(tag, "saveUserPreferences - error: ${exception.message}")
                        continuation.resume(Result.failure(exception))
                    }
            } catch (e: Exception) {
                Log.e(tag, "saveUserPreferences - error: ${e.message}")
                continuation.resume(Result.failure(e))
            }
        }
    }

    override suspend fun updateUserPreferences(userPreferences: UserPreferences): Result<UserPreferences> {
        Log.d(tag, "udateUserPreferences")
        return suspendCancellableCoroutine { continuation ->
            try {
                val newUserPrefences = hashMapOf(
                    "uid" to uid,
                    "name" to userPreferences.name,
                    "age" to userPreferences.age,
                    "country" to userPreferences.country,
                    "interests" to userPreferences.interests,
                    "profession" to userPreferences.profession,
                    "relationship" to userPreferences.relationship,
                    "selectedInterests" to userPreferences.selectedInterests,
                    "selectedRelationships" to userPreferences.selectedRelationships,
                    "about" to userPreferences.about,
                    "skip" to userPreferences.skip,
                )

                collection
                    .document(userPreferences.id)
                    .update(newUserPrefences)
                    .addOnSuccessListener {
                        continuation.resume(Result.success(userPreferences))
                    }
                    .addOnFailureListener {
                        Log.e(tag, "udateUserPreferences - error: ${it.message}")
                        continuation.resume(Result.failure(GenericException.ClientException()))
                    }
            } catch (e: Exception) {
                Log.e(tag, "udateUserPreferences - error: ${e.message}")
                continuation.resume(Result.failure(e))
            }
        }
    }

    override suspend fun getUserPreferences(): Result<UserPreferences> {
        Log.d(tag, "getUserPreferencesBy")

        return suspendCancellableCoroutine { continuation ->
            collection
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    var userPreferences: UserPreferences? = null
                    if (!querySnapshot.isEmpty) {
                        for (document in querySnapshot) {
                            val myDocument =
                                document.toObject(UserPreferences::class.java).copy(id = document.id)
                            userPreferences = myDocument
                        }
                        userPreferences?.let {
                            continuation.resume(Result.success(it))
                        }
                    } else {
                        continuation.resume(Result.failure(UserException.UserNotFound()))
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(tag, "getUserPreferencesBy - error: ${e.message}")
                    continuation.resume(Result.failure(GenericException.ClientException()))
                }
        }
    }
}