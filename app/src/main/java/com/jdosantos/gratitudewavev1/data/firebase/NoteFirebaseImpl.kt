package com.jdosantos.gratitudewavev1.data.firebase

import android.annotation.SuppressLint
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdosantos.gratitudewavev1.domain.models.Note
import com.jdosantos.gratitudewavev1.data.pagination.NotesPagingSource
import com.jdosantos.gratitudewavev1.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class NoteFirebaseImpl @Inject constructor(
    private val auth: FirebaseAuth?,
    db: FirebaseFirestore
) : NoteRepository {
    private val tag = this::class.java.simpleName
    val email: String?
        get() = auth?.currentUser?.email

    private val collection: CollectionReference = db.collection("Notes")

    override fun getNotesByEmail(callback: (List<Note>) -> Unit, onError: () -> Unit) {
        Log.d(tag, "getNotesByEmail")
        if (email != null) {
            collection
                .whereEqualTo("email", email!!.toString())
                .orderBy("createAt", Query.Direction.DESCENDING)

                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        onError.invoke()
                        Log.e(tag, "getNotesByEmail - error: $error")
                        return@addSnapshotListener
                    }

                    val notes = mutableListOf<Note>()
                    if (querySnapshot != null) {
                        for (document in querySnapshot) {
                            val note = mapToState(document)
                            notes.add(note)

                        }
                    }

                    callback.invoke(notes)
                }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun saveNote(note: Note, callback: (success: Boolean) -> Unit) {
        Log.d(tag, "saveNote")
        try {
            val newNote = hashMapOf(
                "note" to note.note,
                "type" to note.type,
                "emotion" to note.emotion,
                "date" to note.date,
                "email" to email,
                "noteTag" to note.noteTag,
                "color" to note.color,
                "createAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )

            collection.add(newNote)
                .addOnSuccessListener { callback.invoke(true) }
                .addOnFailureListener {
                    Log.e(tag, "saveNote - error: ${it.message}")
                    callback.invoke(false)
                }


        } catch (e: Exception) {
            Log.e(tag, "saveNote - error: ${e.message}")
            callback.invoke(false)
        }


    }

    private fun mapToState(document: DocumentSnapshot): Note {
        Log.d(tag, "mapToState")
        val timestampUpdateAt = document.getTimestamp("updateAt")
        val timestampCreateAt = document.getTimestamp("createAt")

        val updateAt = timestampUpdateAt?.toDate()
        val createAt = timestampCreateAt?.toDate()

        return document.toObject(Note::class.java)!!.copy(
            idDoc = document.id,
            updateAt = updateAt,
            createAt = createAt
        )
    }

    override fun updateNote(note: Note, callback: (success: Boolean) -> Unit) {
        Log.d(tag, "updateNote")
        try {
            val editNote = hashMapOf(
                "note" to note.note,
                "type" to note.type,
                "emotion" to note.emotion,
                "noteTag" to note.noteTag,
                "color" to note.color,
                "updateAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )
            Log.d("UPDATE", note.idDoc)
            Log.d("UPDATE", editNote.toString())
            collection
                .document(note.idDoc)
                .update(editNote as Map<String, Any>)
                .addOnSuccessListener {
                    callback.invoke(true)
                }
                .addOnFailureListener {
                    Log.e(tag, "updateNote - error: ${it.message}")
                    callback.invoke(false)
                }


        } catch (e: Exception) {
            Log.e(tag, "updateNote - error: ${e.message}")
            callback.invoke(false)
        }


    }

    override fun getNoteById(
        id: String,
        callback: (note: Note) -> Unit, onError: () -> Unit
    ) {
        Log.d(tag, "getNoteById")
        try {
            collection
                .document(id)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && snapshot.exists()) {
                        val note = mapToState(snapshot)
                        callback.invoke(note)
                    }
                }
        } catch (e: Exception) {
            Log.e(tag, "getNoteById - error: ${e.message}")
            onError.invoke()
        }


    }

    private fun queryNotesPaging() = collection
        .whereEqualTo("email", email!!.toString())
        .orderBy("createAt", Query.Direction.DESCENDING)
        .limit(15)


    override fun getNotesPaging(): Flow<PagingData<Note>> = Pager(
        config = PagingConfig(pageSize = 15),
        pagingSourceFactory = { NotesPagingSource(queryNotesPaging()) }
    ).flow

    override fun getMyNotesByDate(
        date: String,
        callback: (List<Note>) -> Unit,
        onError: () -> Unit
    ) {
        Log.d(tag, "getMyNotesByDate")
        if (email != null) {
            collection
                .whereEqualTo("email", email!!.toString())
                .whereEqualTo("date", date)
                .orderBy("createAt", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        onError.invoke()
                        Log.e(tag, "getMyNotesByDate - error: $error")
                        return@addSnapshotListener
                    }

                    val notes = mutableListOf<Note>()

                    if (querySnapshot != null) {
                        for (document in querySnapshot) {
                            val note = mapToState(document)
                            notes.add(note)

                        }
                    }

                    callback.invoke(notes)
                }
        }

    }

    override fun deleteNoteById(
        id: String,
        callback: (success: Boolean) -> Unit
    ) {
        Log.d(tag, "deleteNoteById")
        try {
            collection.document(id).delete()
            callback.invoke(true)
        } catch (e: Exception) {
            Log.e(tag, "deleteNoteById - error: ${e.message}")
            callback.invoke(false)
        }
    }

    override fun getMyNotesByTag(
        tagId: String,
        callback: (List<Note>) -> Unit,
        onError: () -> Unit
    ) {
        Log.d(tag, "getMyNotesByTag")
        collection
            .whereEqualTo("email", email!!.toString())
            .whereEqualTo("noteTag.id", tagId)
            .orderBy("createAt", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    onError.invoke()
                    Log.e(tag, "getMyNotesByTag - error: $error")
                    return@addSnapshotListener
                }

                val notes = mutableListOf<Note>()

                if (querySnapshot != null) {
                    for (document in querySnapshot) {
                        val note = mapToState(document)
                        notes.add(note)

                    }
                }
                callback.invoke(notes)
            }

    }

    override fun getFirstNoteByEmail(callback: (Note?) -> Unit, onError: () -> Unit) {
        Log.d(tag, "getFirstNoteByEmail")
        if (email != null) {
            collection
                .whereEqualTo("email", email!!.toString())
                .orderBy(
                    "createAt",
                    Query.Direction.ASCENDING
                ) // Ordenar por fecha de creación ascendente
                .limit(1) // Limitar la consulta a una nota (la primera)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val firstNote = querySnapshot.documents.firstOrNull()?.let { mapToState(it) }
                    callback.invoke(firstNote!!)
                }
                .addOnFailureListener {
                    Log.e(tag, "deleteNoteById - error: ${it.message}")
                    onError.invoke()
                }
        }
    }

    override fun getNoteCreationDatesByEmail(callback: (List<Date>) -> Unit, onError: () -> Unit) {
        Log.d(tag, "getNoteCreationDatesByEmail")
        if (email != null) {
            collection
                .whereEqualTo("email", email!!.toString())
                .orderBy("createAt", Query.Direction.ASCENDING)
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        onError.invoke()
                        Log.e(tag, "getNoteCreationDatesByEmail - error: $error")
                        return@addSnapshotListener
                    }

                    val creationDates = querySnapshot!!.documents.mapNotNull { document ->
                        document.getTimestamp("createAt")?.toDate()
                    }
                    callback.invoke(creationDates)
                }
        }
    }

}