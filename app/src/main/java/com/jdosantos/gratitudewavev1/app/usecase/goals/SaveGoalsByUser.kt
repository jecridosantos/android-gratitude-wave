package com.jdosantos.gratitudewavev1.app.usecase.goals

import com.jdosantos.gratitudewavev1.app.model.Goals
import com.jdosantos.gratitudewavev1.app.repository.GoalsRepository
import javax.inject.Inject

class SaveGoalsByUser @Inject constructor(
    private val goalsRepository: GoalsRepository
) {
    fun execute(goals: Goals, onSuccess: () -> Unit, onError: (error: String) -> Unit) {
        if (goals.id.isNullOrEmpty()) {
            goalsRepository.save(goals, onSuccess, onError)
        } else {
            goalsRepository.update(goals, onSuccess, onError)
        }

    }
}