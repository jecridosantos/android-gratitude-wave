package com.jdosantos.gratitudewavev1.app.repository.impl

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
import com.jdosantos.gratitudewavev1.app.paging.NotesPagingSource
import com.jdosantos.gratitudewavev1.app.model.Note
import com.jdosantos.gratitudewavev1.app.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class NoteFirebaseImpl @Inject constructor(
    private val auth: FirebaseAuth?,
    db: FirebaseFirestore
) : NoteRepository {
    val email: String?
        get() = auth?.currentUser?.email

    private val collection: CollectionReference = db.collection("Notes")

    override fun getNotesByEmail(callback: (List<Note>) -> Unit) {
        Log.d("NoteFirebaseImpl", "getNotesByEmail email: ${email}")
        if (email != null) {
            collection
                .whereEqualTo("email", email!!.toString())
                .orderBy("createAt", Query.Direction.DESCENDING)

                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
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
        } else {
            // Handle case where user is not logged in
            callback.invoke(emptyList())
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun saveNote(note: Note, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
        try {
            val newNote = hashMapOf(
                "note" to note.note,
                "type" to note.type,
                "emotion" to note.emotion,
                "date" to note.date,
                "email" to note.email,
                "tag" to note.tag,
                "color" to note.color,
                "createAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )


            collection.add(newNote)
                .addOnSuccessListener { onSuccess() }


        } catch (e: Exception) {
            Log.d("ERROR SAVE", "Error at save new note ${e.localizedMessage}")
            onError("Error al guardar nota")
        }


    }

    private fun mapToState(document: DocumentSnapshot): Note {
        val _updateAt = document.getTimestamp("updateAt")
        val _createAt = document.getTimestamp("createAt")

        val updateAt = _updateAt?.toDate()
        val createAt = _createAt?.toDate()

        return document.toObject(Note::class.java)!!.copy(
            idDoc = document.id,
            updateAt = updateAt,
            createAt = createAt
        )
    }

    override fun updateNote(note: Note, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
        try {
            val editNote = hashMapOf(
                "note" to note.note,
                "type" to note.type,
                "emotion" to note.emotion,
                "tag" to note.tag,
                "color" to note.color,
                "updateAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )
            Log.d("UPDATE", note.idDoc)
            Log.d("UPDATE", editNote.toString())
            collection
                .document(note.idDoc)
                .update(editNote as Map<String, Any>)
                .addOnSuccessListener {
                    onSuccess()
                }


        } catch (e: Exception) {
            Log.d("ERROR UPDATE", "Error at update new note ${e.localizedMessage}")
            onError("Error al actualizar nota")
        }


    }

    override fun getNoteById(
        id: String,
        onSuccess: (note: Note) -> Unit,
        onError: (error: String) -> Unit
    ) {
        try {
            collection
                .document(id)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && snapshot.exists()) {
                        val note = mapToState(snapshot)
                        onSuccess(note)
                    } else {
                        onError("Error")
                    }
                }
        } catch (e: Exception) {
            Log.d("NoteFirebaseImpl", e.localizedMessage)
            onError("Error")
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

    override fun getMyNotesByDate(date: String, callback: (List<Note>) -> Unit) {
        Log.d("NoteFirebaseImpl", "getMyNotesByDate email: ${email}")
        if (email != null) {
            collection
                .whereEqualTo("email", email!!.toString())
                .whereEqualTo("date", date)
                .orderBy("createAt", Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
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
        } else {
            // Handle case where user is not logged in
            callback.invoke(emptyList())
        }

    }

    override fun deleteNoteById(
        id: String,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit
    ) {
        try {
            collection.document(id).delete()
            onSuccess()
        } catch (e: Exception) {
            Log.d("ERROR DELETE", "Error at delete new note ${e.localizedMessage}")
            onError("Error al eliminar nota")
        }
    }

    override fun getMyNotesByTag(tagId: String, callback: (List<Note>) -> Unit) {

        collection
            .whereEqualTo("email", email!!.toString())
            .whereEqualTo("tag.id", tagId)
            .orderBy("createAt", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val notes = mutableListOf<Note>()

                if (querySnapshot != null) {
                    for (document in querySnapshot) {
  /*                      val note =
                            document.toObject(Note::class.java).copy(idDoc = document.id)*/

                        val note = mapToState(document)
                        notes.add(note)

                    }
                }

                callback.invoke(notes)
            }

    }

    override fun getFirstNoteByEmail(callback: (Note?) -> Unit, onError: (error: String)-> Unit) {
        Log.d("NoteFirebaseImpl", "getFirstNoteByEmail email: $email")
        if (email != null) {
            collection
                .whereEqualTo("email", email!!.toString())
                .orderBy("createAt", Query.Direction.ASCENDING) // Ordenar por fecha de creación ascendente
                .limit(1) // Limitar la consulta a una nota (la primera)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val firstNote = querySnapshot.documents.firstOrNull()?.let { mapToState(it) }
                    callback.invoke(firstNote!!)
                }
                .addOnFailureListener { exception ->
                    Log.e("NoteFirebaseImpl", "Error getting first note: ", exception)
                    onError.invoke("Error")
                }
        } else {
            // Manejar el caso donde el usuario no ha iniciado sesión
            onError.invoke("Error")
        }
    }

    override fun getNoteCreationDatesByEmail(callback: (List<Date>) -> Unit, onError: (error: String)-> Unit) {
        Log.d("NoteFirebaseImpl", "getNoteCreationDatesByEmail email: $email")
        if (email != null) {
            collection
                .whereEqualTo("email", email!!.toString())
                .orderBy("createAt", Query.Direction.ASCENDING) // Ordenar por fecha de creación ascendente
               // .get()
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }

                    val creationDates = querySnapshot!!.documents.mapNotNull { document ->
                        document.getTimestamp("createAt")?.toDate()
                    }
                    callback.invoke(creationDates)
                }
            /*    .addOnFailureListener { exception ->
                    Log.e("NoteFirebaseImpl", "Error getting note creation dates: ", exception)
                    onError.invoke("Error")
                }*/
        } else {
            // Manejar el caso donde el usuario no ha iniciado sesión
            onError.invoke("Error")
        }
    }

}