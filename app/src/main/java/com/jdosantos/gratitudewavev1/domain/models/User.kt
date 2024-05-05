package com.jdosantos.gratitudewavev1.domain.models

sealed interface User {
    object LoggedOut : User

    data class LoggedIn(
        val data: UserData,
    ) : User
}