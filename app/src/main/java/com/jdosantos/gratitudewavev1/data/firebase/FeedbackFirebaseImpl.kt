package com.jdosantos.gratitudewavev1.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdosantos.gratitudewavev1.domain.models.Feedback
import com.jdosantos.gratitudewavev1.domain.repository.FeedbackRepository
import javax.inject.Inject

class FeedbackFirebaseImpl @Inject constructor(
    private val auth: FirebaseAuth?,
    db: FirebaseFirestore
) : FeedbackRepository {
    private val tag = this::class.java.simpleName
    private val collection: CollectionReference = db.collection("Feedback")
    private val uid: String?
        get() = auth?.currentUser?.uid

    override fun save(feedback: Feedback, callback: (success: Boolean) -> Unit) {
        Log.d(tag, "save")
        try {
            val newDocument = hashMapOf(
                "uid" to uid,
                "checks" to feedback.checks,
                "message" to feedback.message,
                "createAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )

            collection.add(newDocument)
                .addOnSuccessListener { callback.invoke(true) }
                .addOnFailureListener {
                    callback.invoke(false)
                    Log.e(tag, "save - error: ${it.message}")
                }

        } catch (e: Exception) {
            callback.invoke(false)
            Log.e(tag, "save - error: ${e.message}")
        }
    }
}