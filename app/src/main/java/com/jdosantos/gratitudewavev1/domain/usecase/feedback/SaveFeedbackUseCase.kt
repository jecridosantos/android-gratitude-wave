package com.jdosantos.gratitudewavev1.domain.usecase.feedback

import com.jdosantos.gratitudewavev1.domain.models.Feedback
import com.jdosantos.gratitudewavev1.domain.repository.FeedbackRepository
import javax.inject.Inject

class SaveFeedbackUseCase @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) {

    fun execute(feedback: Feedback, callback: (success: Boolean) -> Unit) {
        feedbackRepository.save(feedback, callback)
    }
}