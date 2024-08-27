package com.example.recipemate.utils

object Validation {

    fun validateInput(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String
    ): Boolean {
        return when {
            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> {
                false
            }

            password.isEmpty() || password.length < 8 || !password.any { it.isDigit() } -> {
                false
            }

            firstName.isEmpty() || !firstName.matches(Regex("^[A-Za-z]+$")) -> {
                false
            }

            lastName.isEmpty() || !lastName.matches(Regex("^[A-Za-z]+$")) -> {
                false
            }

            phoneNumber.isEmpty() || !phoneNumber.matches(Regex("^\\+?[0-9]{10,15}$")) -> {
                false
            }

            else -> true
        }
    }
}