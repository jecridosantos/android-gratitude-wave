package com.jdosantos.gratitudewavev1.data.firebase

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.domain.repository.TagRepository
import javax.inject.Inject

class TagFirebaseRepository @Inject constructor(
    db: FirebaseFirestore
) : TagRepository {
    private val tag = this::class.java.simpleName
    private val collection: CollectionReference = db.collection("Tags")

    override fun getTags(callback: (List<NoteTag>) -> Unit, onError: () -> Unit) {
        Log.d(tag, "getTags")
        try {
            collection
                .whereEqualTo("enable", true)
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        onError.invoke()
                        Log.e(tag, "getNotesByEmail - error: $error")
                        return@addSnapshotListener
                    }

                    val noteTags = mutableListOf<NoteTag>()

                    if (querySnapshot != null) {
                        for (document in querySnapshot) {
                            val myDocument =
                                document.toObject(NoteTag::class.java).copy(id = document.id)
                            noteTags.add(myDocument)

                        }
                    }

                    callback.invoke(noteTags)
                }
        } catch (e: Exception) {
            Log.e(tag, "getTags - error: ${e.message}")
            onError.invoke()
        }
    }

    override fun getTagById(
        id: String,
        callback: (noteTag: NoteTag) -> Unit,
        onError: () -> Unit
    ) {
        Log.d(tag, "getTagById")
        try {
            collection
                .document(id)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && snapshot.exists()) {
                        callback.invoke(
                            snapshot.toObject(NoteTag::class.java)!!.copy(id = snapshot.id)
                        )
                    }
                }
        } catch (e: Exception) {
            Log.e(tag, "getTagById - error: ${e.message}")
            onError.invoke()
        }

    }
}