package com.jdosantos.gratitudewavev1.domain.exceptions

sealed class GenericException(message: String) : Exception(message) {
    class ClientException : GenericException("Ha ocurrido un error inesperado.")
}