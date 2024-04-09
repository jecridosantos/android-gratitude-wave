package com.jdosantos.gratitudewavev1.app.usecase

import com.jdosantos.gratitudewavev1.app.model.Tag
import com.jdosantos.gratitudewavev1.app.repository.TagRepository
import javax.inject.Inject

class GetTagByIdUseCase @Inject constructor(
    private val tagRepository: TagRepository
) {

    fun execute(id: String, callback: (Tag) -> Unit, error: (String) -> Unit) {
        tagRepository.getTagById(id, {
            callback(it)
        }) {
            error(it)
        }

    }
}