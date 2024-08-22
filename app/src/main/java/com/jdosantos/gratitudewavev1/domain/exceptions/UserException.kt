package com.jdosantos.gratitudewavev1.domain.exceptions

sealed class UserException(message: String) : Exception(message) {
    class UserNotFound : UserException("Usuario no encontrado.")

    class UserDefaultError : UserException("Error al consultar usuario.")

    class UserNameNotFound: UserException("Error al consultar nombre de usuario.")

    class UserSaveError : UserException("Error al guardar usuario.")
}
