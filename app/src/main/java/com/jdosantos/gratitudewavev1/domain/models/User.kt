package com.jdosantos.gratitudewavev1.domain.models

data class User(
    val uid: String = "",
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val createAt: Any? = "",
    val updateAt: Any? = "",
    val photoUrl: String? = null,
    val provider: String = ""
)
