package com.jdosantos.gratitudewavev1.app.repository.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdosantos.gratitudewavev1.app.model.User
import com.jdosantos.gratitudewavev1.app.repository.UserRepository
import javax.inject.Inject

class UserFirebaseImpl @Inject constructor(
    private val auth: FirebaseAuth?,
    db: FirebaseFirestore
) : UserRepository {

    private val uid: String?
        get() = auth?.currentUser?.uid

    private val collection: CollectionReference = db.collection("Users")

    override fun saveUser(user: User, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
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
                .addOnSuccessListener { onSuccess() }


        } catch (e: Exception) {
            Log.d("ERROR SAVE", "Error at save new user ${e.localizedMessage}")
            onError("Error al guardar usuario")
        }
    }

    override fun getUserById(
        id: String,
        onSuccess: (user: User) -> Unit,
        onError: (error: String) -> Unit
    ) {
        try {
            collection
                .document(id)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && snapshot.exists()) {
                        onSuccess(snapshot.toObject(User::class.java)!!)
                    } else {
                        onError("Error")
                    }
                }
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("NoteFirebaseImpl", it) }
            onError("Error")
        }
    }

    override fun getUserByUid(
        uid: String,
        onSuccess: (user: User?) -> Unit,
        onError: (error: String) -> Unit
    ) {
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
                    onSuccess(user)
                } else {
                    onError("not found")
                }

            }
            .addOnFailureListener { exception ->
                onError("error")
            }
    }
}