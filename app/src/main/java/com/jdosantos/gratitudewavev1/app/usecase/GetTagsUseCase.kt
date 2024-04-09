package com.jdosantos.gratitudewavev1.app.usecase

import com.jdosantos.gratitudewavev1.app.model.Tag
import com.jdosantos.gratitudewavev1.app.repository.TagRepository
import javax.inject.Inject

class GetTagsUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {
    fun execute(callback: (List<Tag>) -> Unit) {
        tagRepository.getTags {
            callback(it)
        }
    }
}