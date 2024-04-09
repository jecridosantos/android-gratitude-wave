package com.jdosantos.gratitudewavev1.app.usecase.goals

import com.jdosantos.gratitudewavev1.app.model.Goals
import com.jdosantos.gratitudewavev1.app.repository.GoalsRepository
import javax.inject.Inject

class GetGoalsByUser @Inject constructor(
    private val goalsRepository: GoalsRepository
) {
    fun execute(callback: (Goals) -> Unit) {
        goalsRepository.getByUser {
            callback(it)
        }
    }
}