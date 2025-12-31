package com.masterproject.englishapp.utils

object Validators {
    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$".toRegex()

    fun isValidEmail(email: String): Boolean {
        return email.matches(EMAIL_REGEX)
    }
}