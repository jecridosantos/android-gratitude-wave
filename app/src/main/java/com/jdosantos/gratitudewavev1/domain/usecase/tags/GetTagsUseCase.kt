package com.jdosantos.gratitudewavev1.domain.usecase.tags

import com.jdosantos.gratitudewavev1.domain.models.NoteTag
import com.jdosantos.gratitudewavev1.domain.repository.TagRepository
import javax.inject.Inject

class GetTagsUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    fun execute(callback: (List<NoteTag>) -> Unit, onError: () -> Unit) {
        tagRepository.getTags(callback, onError)
    }
}