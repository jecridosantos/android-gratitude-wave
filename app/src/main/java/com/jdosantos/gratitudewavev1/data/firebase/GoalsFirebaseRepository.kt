package com.jdosantos.gratitudewavev1.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdosantos.gratitudewavev1.domain.models.Goals
import com.jdosantos.gratitudewavev1.domain.repository.GoalsRepository
import javax.inject.Inject

class GoalsFirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth?,
    db: FirebaseFirestore
) : GoalsRepository {
    private val tag = this::class.java.simpleName
    private val uid: String?
        get() = auth?.currentUser?.uid


    private val collection: CollectionReference = db.collection("Goals")

    override fun save(goals: Goals, callback: (Boolean) -> Unit) {
        Log.d(tag, "save")
        try {
            val newGoals = hashMapOf(
                "uid" to uid,
                "challenge" to goals.challenge,
                "createAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )

            collection.add(newGoals)
                .addOnSuccessListener { callback.invoke(true) }
                .addOnFailureListener {
                    Log.e(tag, "save - error: ${it.message}")
                    callback.invoke(true)
                }

        } catch (e: Exception) {
            Log.e(tag, "save - error: ${e.message}")
            callback.invoke(false)
        }
    }

    override fun getByUser(callback: (Goals) -> Unit, onError: () -> Unit) {
        Log.d(tag, "getByUser")
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
                        callback(goals)
                    }
                }
                .addOnFailureListener {
                    Log.e(tag, "getByUser - error: ${it.message}")
                    onError.invoke()
                }
        }
    }

    override fun update(goals: Goals, callback: (Boolean) -> Unit) {
        Log.d(tag, "update")
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
                    callback.invoke(true)
                }.addOnFailureListener {
                    Log.e(tag, "update - error: ${it.message}")
                    callback.invoke(false)
                }


        } catch (e: Exception) {
            Log.e(tag, "update - error: ${e.message}")
            callback.invoke(false)
        }
    }
}