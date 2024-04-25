package com.jdosantos.gratitudewavev1.domain.models

data class User(
    val uid: String = "",
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val createAt: Any? = "",
    val photoUrl: Any? = null,
    val provider: String = ""
)
