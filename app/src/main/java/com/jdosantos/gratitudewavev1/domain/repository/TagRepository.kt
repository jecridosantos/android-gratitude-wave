package com.jdosantos.gratitudewavev1.domain.repository

import com.jdosantos.gratitudewavev1.domain.models.NoteTag

interface TagRepository {
    fun getTags(callback: (List<NoteTag>) -> Unit, onError: ()-> Unit)

    fun getTagById(id: String, callback: (noteTag: NoteTag) -> Unit, onError: ()-> Unit)
}