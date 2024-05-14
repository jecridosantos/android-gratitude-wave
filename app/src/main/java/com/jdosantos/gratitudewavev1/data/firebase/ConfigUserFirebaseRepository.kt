package com.jdosantos.gratitudewavev1.data.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdosantos.gratitudewavev1.domain.models.UserSettings
import com.jdosantos.gratitudewavev1.domain.repository.ConfigUserRepository
import javax.inject.Inject

class ConfigUserFirebaseRepository
@Inject constructor(
    private val auth: FirebaseAuth?,
    db: FirebaseFirestore
) : ConfigUserRepository {
    private val tag = this::class.java.simpleName
    private val uid: String?
        get() = auth?.currentUser?.uid


    private val collection: CollectionReference = db.collection("UserSettings")

    override fun save(body: UserSettings, callback: (success: Boolean) -> Unit) {
        Log.d(tag, "save")
        try {
            val newDocument = hashMapOf(
                "uid" to uid,
                "muteNotifications" to body.muteNotifications,
                "reminders" to body.reminders,
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

    override fun getByUser(callback: (UserSettings) -> Unit, onError: ()-> Unit) {
        Log.d(tag, "getByUser")
        if (uid != null) {
            collection
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    var userSettings = UserSettings()
                    if (querySnapshot != null) {
                        for (document in querySnapshot) {
                            val myDocument =
                                document.toObject(UserSettings::class.java).copy(id = document.id)
                            userSettings = myDocument

                        }
                        Log.d(tag, "getByUser - userSettings: $userSettings")
                        callback.invoke(userSettings)
                    }
                }
                .addOnFailureListener { exception ->
                    onError.invoke()
                    Log.e(tag, "getByUser - error: ${exception.message}")
                }
        }
    }

    override fun update(body: UserSettings, callback: (success: Boolean) -> Unit) {
        Log.d(tag, "update")
        try {
            val editMap = hashMapOf(
                "id" to body.id,
                "uid" to body.uid,
                "muteNotifications" to body.muteNotifications,
                "reminders" to body.reminders,
                "updateAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )
            collection
                .document(body.id!!)
                .update(editMap as Map<String, Any>)
                .addOnSuccessListener {
                    callback.invoke(true)
                }.addOnFailureListener {
                    callback.invoke(false)
                    Log.e(tag, "update - error: ${it.message}")
                }


        } catch (e: Exception) {
            callback.invoke(false)
            Log.e(tag, "update - error: ${e.message}")
        }
    }
}