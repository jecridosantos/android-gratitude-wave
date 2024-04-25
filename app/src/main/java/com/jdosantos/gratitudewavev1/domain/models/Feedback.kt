package com.jdosantos.gratitudewavev1.domain.models

data class Feedback(
    val uid: String? = "",
    val checks: MutableList<String>? = mutableListOf(),
    val message: String? = "",
)
