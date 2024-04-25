package com.jdosantos.gratitudewavev1.domain.usecase.goals

import com.jdosantos.gratitudewavev1.domain.models.Goals
import com.jdosantos.gratitudewavev1.domain.repository.GoalsRepository
import javax.inject.Inject

class GetGoalsByUserUseCase @Inject constructor(
    private val goalsRepository: GoalsRepository
) {
    fun execute(callback: (Goals) -> Unit, onError: () -> Unit) {
        goalsRepository.getByUser(
            callback,
            onError
        )
    }
}