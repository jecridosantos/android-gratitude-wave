package com.jdosantos.gratitudewavev1.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdosantos.gratitudewavev1.domain.models.UserData
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

    override fun saveUser(userData: UserData, callback: (success: Boolean) -> Unit) {
        Log.d(tag, "saveUser")
        try {

            val newNote = hashMapOf(
                "uid" to uid,
                "email" to userData.email,
                "name" to userData.name,
                "photoUrl" to userData.photoUrl,
                "provider" to userData.provider,
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
        callback: (userData: UserData) -> Unit, onError: () -> Unit
    ) {
        Log.d(tag, "getUserById")

        try {
            collection
                .document(id)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && snapshot.exists()) {
                        callback.invoke(snapshot.toObject(UserData::class.java)!!)
                    }
                }
        } catch (e: Exception) {
            Log.e(tag, "getUserById - error: ${e.message}")
            onError.invoke()
        }
    }

    override fun getUserByUid(
        uid: String,
        callback: (userData: UserData?) -> Unit, onError: () -> Unit
    ) {
        Log.d(tag, "getUserByUid")
        collection
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                var userData = UserData()
                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val myDocument = document.toObject(UserData::class.java).copy(id = document.id)
                        userData = myDocument

                    }
                    callback.invoke(userData)
                }

            }
            .addOnFailureListener {
                Log.e(tag, "getUserByUid - error: ${it.message}")
                onError.invoke()
            }
    }
}