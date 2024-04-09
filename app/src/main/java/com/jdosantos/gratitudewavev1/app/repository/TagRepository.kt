package com.jdosantos.gratitudewavev1.app.repository

import com.jdosantos.gratitudewavev1.app.model.Tag

interface TagRepository {
    fun getTags(callback: (List<Tag>) -> Unit)

    fun getTagById(id: String, onSuccess: (tag: Tag) -> Unit, onError: (error: String) -> Unit)
}