package com.jdosantos.gratitudewavev1.app.repository.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdosantos.gratitudewavev1.app.model.ConfigUser
import com.jdosantos.gratitudewavev1.app.repository.ConfigUserRepository
import javax.inject.Inject

class ConfigUserFirebaseImpl
@Inject constructor(
    private val auth: FirebaseAuth?,
    db: FirebaseFirestore
) : ConfigUserRepository {

    private val uid: String?
        get() = auth?.currentUser?.uid


    private val collection: CollectionReference = db.collection("ConfigUser")

    override fun save(body: ConfigUser, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
        try {
            val newDocument = hashMapOf(
                "uid" to uid,
                "muteNotifications" to body.muteNotifications,
                "reminders" to body.reminders,
                "createAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )

            collection.add(newDocument)
                .addOnSuccessListener { onSuccess() }

        } catch (e: Exception) {
            Log.d("ERROR SAVE", "ConfigUserRepository - save - ${e.localizedMessage}")
            onError("ConfigUserRepository - save")
        }
    }

    override fun getByUser(callback: (ConfigUser) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun update(body: ConfigUser, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
        TODO("Not yet implemented")
    }
}