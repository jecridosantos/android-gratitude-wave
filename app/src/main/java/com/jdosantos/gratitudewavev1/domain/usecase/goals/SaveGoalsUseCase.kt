package com.jdosantos.gratitudewavev1.domain.usecase.goals

import com.jdosantos.gratitudewavev1.domain.models.Goals
import com.jdosantos.gratitudewavev1.domain.repository.GoalsRepository
import javax.inject.Inject

class SaveGoalsUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository
) {
    fun execute(goals: Goals, callback: (Boolean) -> Unit) {
        if (goals.id.isNullOrEmpty()) {
            goalsRepository.save(goals, callback)
        } else {
            goalsRepository.update(goals, callback)
        }

    }
}