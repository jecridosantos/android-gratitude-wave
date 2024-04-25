package com.jdosantos.gratitudewavev1.domain.usecase.tags

import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.domain.repository.TagRepository
import javax.inject.Inject

class GetTagByIdUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {

    fun execute(id: String, callback: (NoteTag) -> Unit, onError: () -> Unit) {
        tagRepository.getTagById(id, callback, onError)

    }
}