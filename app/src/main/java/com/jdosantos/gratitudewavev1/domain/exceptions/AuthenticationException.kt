package com.jdosantos.gratitudewavev1.domain.exceptions

sealed class AuthenticationException(message: String) : Exception(message) {
    class InvalidCredentialsException : AuthenticationException("Credenciales incorrectas. Verifica tu correo y contraseña e intenta de nuevo.")
    class GenericAuthenticationException : AuthenticationException("Ha ocurrido un error en la autenticación. Por favor, intenta de nuevo más tarde.")
    class EmailNotVerifiedException : AuthenticationException("Correo no verificado.")
    class UserNotLogged: AuthenticationException("No hay usuario.")
    class AuthUserCollisionException: AuthenticationException("El correo electrónico ya está siendo usado.")
    class EmailNotFoundException: AuthenticationException("El correo proporcionado no está registrado")

    class SignUpException: AuthenticationException("No se pudo registrar usuario.")

}
