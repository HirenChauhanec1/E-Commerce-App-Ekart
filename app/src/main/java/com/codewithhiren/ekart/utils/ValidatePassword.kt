package com.codewithhiren.ekart.utils

import java.util.regex.Pattern

object ValidatePassword {

    val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^"
                + "(?=.*[0-9])" //minimum one number
                + "(?=.*[a-z])" //minimum one lower case character
                + "(?=.*[A-Z])" //minimum one UPPER case character
                + "(?=.*[a-zA-Z])" //any character
                + "(?=.*[@#$%^&+=])" //minimum one special character
                + "(?=\\S+$)" //no white spaces
                + ".{6,}" //minimum length 6 characters
                + "$"
    )
     val PASSWORD_UPPERCASE_PATTERN: Pattern = Pattern.compile( "(?=.*[A-Z])" + ".{6,}")

    val PASSWORD_LOWERCASE_PATTERN: Pattern = Pattern.compile( "(?=.*[a-z])" + ".{6,}")

    val PASSWORD_NUMBER_PATTERN: Pattern = Pattern.compile("(?=.*[0-9])" + ".{6,}")

    val PASSWORD_SPECIAL_CHARACTER_PATTERN: Pattern = Pattern.compile("(?=.*[@#$%^&+=])" + ".{6,}")

    val PASSWORD_MINIMUM_6_CHARACTER: Pattern = Pattern.compile(".{6,}")

}