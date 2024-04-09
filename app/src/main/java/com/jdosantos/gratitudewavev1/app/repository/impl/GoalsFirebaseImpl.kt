package com.jdosantos.gratitudewavev1.app.repository.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdosantos.gratitudewavev1.app.model.Goals
import com.jdosantos.gratitudewavev1.app.repository.GoalsRepository
import javax.inject.Inject

class GoalsFirebaseImpl @Inject constructor(
    private val auth: FirebaseAuth?,
    db: FirebaseFirestore
) : GoalsRepository {

    private val uid: String?
        get() = auth?.currentUser?.uid


    private val collection: CollectionReference = db.collection("Goals")

    override fun save(goals: Goals, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
        try {
            val newGoals = hashMapOf(
                "uid" to uid,
                "challenge" to goals.challenge,
                "createAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )

            collection.add(newGoals)
                .addOnSuccessListener { onSuccess() }

        } catch (e: Exception) {
            Log.d("ERROR SAVE", "Error at save new goals ${e.localizedMessage}")
            onError("Error al guardar meta")
        }
    }

    override fun getByUser(callback: (Goals) -> Unit) {
        if (uid != null) {
            collection
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    var goals = Goals()
                    if (querySnapshot != null) {
                        for (document in querySnapshot) {
                            val myDocument =
                                document.toObject(Goals::class.java).copy(id = document.id)
                            goals = myDocument

                        }
                        Log.d("GET GOALS", "my goals: $goals")
                        callback(goals)
                    }
                }
                .addOnFailureListener { exception ->
                }
        } else {
            // Handle case where user is not logged in

        }
    }

    override fun update(goals: Goals, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
        try {
            val editGoals = hashMapOf(
                "id" to goals.id,
                "uid" to goals.uid,
                "challenge" to goals.challenge,
                "updateAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )
            collection
                .document(goals.id!!)
                .update(editGoals as Map<String, Any>)
                .addOnSuccessListener {
                    onSuccess()
                }


        } catch (e: Exception) {
            Log.d("ERROR UPDATE", "Error at update goals ${e.localizedMessage}")
            onError("Error al actualizar meta")
        }
    }
}