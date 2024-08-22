package com.jdosantos.gratitudewavev1.utils

fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isNameValid(name: String): Boolean {
    return name.isNotEmpty()
}

fun validatePassword(password: String, name: String, email: String): Boolean {
    val hasUppercase = password.any { it.isUpperCase() }
    val hasNumber = password.any { it.isDigit() }
    val hasNoWhitespace = !password.contains(Regex("\\s"))
    val doesNotContainName = !password.contains(name, ignoreCase = true)
    val doesNotContainEmail = !password.contains(email, ignoreCase = true)
    val isLongEnough = password.length >= 8

    return hasUppercase && hasNumber && hasNoWhitespace && doesNotContainName && doesNotContainEmail && isLongEnough

}