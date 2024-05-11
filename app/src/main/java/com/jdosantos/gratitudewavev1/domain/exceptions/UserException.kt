package com.jdosantos.gratitudewavev1.domain.exceptions

sealed class UserException(message: String) : Exception(message) {
    class UserNotFound : UserException("Usuario no encontrado.")
}
