package com.jdosantos.gratitudewavev1.app.repository

import com.jdosantos.gratitudewavev1.app.model.Goals

interface GoalsRepository {
    fun save(goals: Goals, onSuccess: () -> Unit, onError: (error: String) -> Unit)

    fun getByUser(callback: (Goals) -> Unit)

    fun update(note: Goals, onSuccess: () -> Unit, onError: (error: String) -> Unit)
}