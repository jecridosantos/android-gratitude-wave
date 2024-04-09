package com.jdosantos.gratitudewavev1.app.repository.impl

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdosantos.gratitudewavev1.app.model.Tag
import com.jdosantos.gratitudewavev1.app.repository.TagRepository
import javax.inject.Inject

class TagFirebaseImpl @Inject constructor(
    db: FirebaseFirestore
) : TagRepository {

    private val collection: CollectionReference = db.collection("Tags")

    override fun getTags(callback: (List<Tag>) -> Unit) {
        collection
            .whereEqualTo("enable", true)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val tags = mutableListOf<Tag>()

                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val myDocument =
                            document.toObject(Tag::class.java).copy(id = document.id)
                        tags.add(myDocument)

                    }
                }

                callback.invoke(tags)
            }
    }

    override fun getTagById(
        id: String,
        onSuccess: (tag: Tag) -> Unit,
        onError: (error: String) -> Unit
    ) {
        try {
            collection
                .document(id)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && snapshot.exists()) {
                        onSuccess(snapshot.toObject(Tag::class.java)!!.copy(id = snapshot.id))
                    } else {
                        onError("Error")
                    }
                }
        } catch (e: Exception) {
            onError("Error")
        }

    }
}