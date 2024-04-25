package com.jdosantos.gratitudewavev1.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdosantos.gratitudewavev1.domain.models.User
import com.jdosantos.gratitudewavev1.domain.repository.UserRepository
import javax.inject.Inject

class UserFirebaseImpl @Inject constructor(
    private val auth: FirebaseAuth?,
    db: FirebaseFirestore
) : UserRepository {
    private val tag = this::class.java.simpleName
    private val uid: String?
        get() = auth?.currentUser?.uid

    private val collection: CollectionReference = db.collection("Users")

    override fun saveUser(user: User, callback: (success: Boolean) -> Unit) {
        Log.d(tag, "saveUser")
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
                .addOnSuccessListener { callback.invoke(true) }
                .addOnFailureListener {
                    Log.e(tag, "saveUser - error: ${it.message}")
                    callback.invoke(false)
                }


        } catch (e: Exception) {
            Log.e(tag, "saveUser - error: ${e.message}")
            callback.invoke(false)
        }
    }

    override fun getUserById(
        id: String,
        callback: (user: User) -> Unit, onError: () -> Unit
    ) {
        Log.d(tag, "getUserById")

        try {
            collection
                .document(id)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && snapshot.exists()) {
                        callback.invoke(snapshot.toObject(User::class.java)!!)
                    }
                }
        } catch (e: Exception) {
            Log.e(tag, "getUserById - error: ${e.message}")
            onError.invoke()
        }
    }

    override fun getUserByUid(
        uid: String,
        callback: (user: User?) -> Unit, onError: () -> Unit
    ) {
        Log.d(tag, "getUserByUid")
        collection
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                var user = User()
                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val myDocument = document.toObject(User::class.java).copy(id = document.id)
                        user = myDocument

                    }
                    callback.invoke(user)
                }

            }
            .addOnFailureListener {
                Log.e(tag, "getUserByUid - error: ${it.message}")
                onError.invoke()
            }
    }
}