package com.jdosantos.gratitudewavev1.domain.repository

import com.jdosantos.gratitudewavev1.domain.models.Goals

interface GoalsRepository {
    fun save(goals: Goals, callback: (success: Boolean) -> Unit)

    fun getByUser(callback: (Goals) -> Unit, onError: () -> Unit)

    fun update(goals: Goals, callback: (success: Boolean) -> Unit)
}