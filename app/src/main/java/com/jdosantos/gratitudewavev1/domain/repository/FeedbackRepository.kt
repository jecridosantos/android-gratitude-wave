package com.jdosantos.gratitudewavev1.domain.repository

import com.jdosantos.gratitudewavev1.domain.models.Feedback

interface FeedbackRepository {
    fun save(feedback: Feedback, callback: (success: Boolean) -> Unit)
}