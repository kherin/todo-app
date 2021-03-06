package com.example.kherindo.utils

import java.util.regex.Pattern

object Validations {
    private val emailPattern: String = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+"
    private val min_one_letter = "*[a-zA-Z]"
    private val mine_one_special_character = "(?=.*[@#\$%^&+=])"
    private val no_whitespace = "(?=\\\\S+\$)"


    fun isEmailValid(value: String): Pair<Boolean, String?> {
        val isValid = Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(value).matches();

        val errorMessage = if(isValid) null else "Email format is incorrect"
        return Pair(isValid, errorMessage)
    }

    fun isPasswordValid(value: String): Pair<Boolean, String?> {
        var valid = true

        // Password policy check
        // Password should be minimum minimum 8 characters long
        if (value.length < 8) {
            valid = false
        }
        // Password should contain at least one number
        var exp = ".*[0-9].*"
        var pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE)
        var matcher = pattern.matcher(value)
        if (!matcher.matches()) {
            valid = false
        }

        // Password should contain at least one capital letter
        exp = ".*[A-Z].*"
        pattern = Pattern.compile(exp)
        matcher = pattern.matcher(value)
        if (!matcher.matches()) {
            valid = false
        }

        // Password should contain at least one small letter
        exp = ".*[a-z].*"
        pattern = Pattern.compile(exp)
        matcher = pattern.matcher(value)
        if (!matcher.matches()) {
            valid = false
        }

        // Password should contain at least one special character
        // Allowed special characters : "~!@#$%^&*()-_=+|/,."';:{}[]<>?"
        exp = ".*[~!@#\$%\\^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?].*"
        pattern = Pattern.compile(exp)
        matcher = pattern.matcher(value)
        if (!matcher.matches()) {
            valid = false
        }
        val errorMessage = if (valid) null else "Password does not meet criteria. example: 221123Awert&"
        return Pair(valid, errorMessage)
    }

    fun doPasswordsMatch(passwordValue: String, confirmPasswordValue: String): Pair<Boolean, String?> {
        return if ( passwordValue == confirmPasswordValue ) Pair(true, null) else Pair(false, "Passwords do not match")
    }

}