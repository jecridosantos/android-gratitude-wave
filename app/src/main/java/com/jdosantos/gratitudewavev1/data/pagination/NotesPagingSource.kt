package com.jdosantos.gratitudewavev1.data.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jdosantos.gratitudewavev1.domain.models.Note
import kotlinx.coroutines.tasks.await

class NotesPagingSource(
    private val queryProductsByName: Query
) : PagingSource<QuerySnapshot, Note>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Note>): QuerySnapshot? = null
    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Note> =
        try {

            val currentPage = params.key ?: queryProductsByName
                .get()
                .await()

            val lastVisibleProduct = if (currentPage.size() > 0) {
                currentPage.documents[currentPage.size() - 1]
            } else {
                null // or handle this case accordingly
            }
            Log.d("Paging souce size", "Size: ${currentPage.size()}")


            val nextPage = if (lastVisibleProduct != null) {
                queryProductsByName.startAfter(lastVisibleProduct).get().await()
            } else {
                null
            }

            // Log.d("Paging nextPage", "Size: ${nextPage.size()}")
            val notes = currentPage.map { note ->
                note.toObject(Note::class.java).copy(idDoc = note.id)
            }
            Log.d("Paging souce", "New data has arrived!")
            LoadResult.Page(
                data = notes,
                prevKey = null,
                nextKey = nextPage
            )

        } catch (e: Exception) {
            Log.d("Paging error", e.localizedMessage)
            LoadResult.Error(e)
        }
}