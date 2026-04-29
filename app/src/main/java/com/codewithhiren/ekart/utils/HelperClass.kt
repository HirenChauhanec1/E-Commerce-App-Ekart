package com.codewithhiren.ekart.utils

import android.util.Patterns
import com.codewithhiren.ekart.model.CartProduct

object HelperClass {


    fun validateEmail(email: String): Pair<Boolean, String> {
        return when {
            email.isEmpty() -> Pair(false, "Enter Email")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Pair(false, "Enter proper email")
            else -> Pair(true, "")
        }
    }

    fun validatePassword(password: String): Pair<Boolean, String> {
        return when {
            password.isEmpty() -> Pair(false, "Enter password")
            !ValidatePassword.PASSWORD_MINIMUM_6_CHARACTER.matcher(password).matches() -> Pair(false, "Minimum 6 character is required")
            !ValidatePassword.PASSWORD_UPPERCASE_PATTERN.matcher(password).matches() -> Pair(false, "One uppercase character is required")
            !ValidatePassword.PASSWORD_LOWERCASE_PATTERN.matcher(password).matches() -> Pair(false, "One lowercase character is required")
            !ValidatePassword.PASSWORD_SPECIAL_CHARACTER_PATTERN.matcher(password).matches() -> Pair(false, "One special character is required")
            !ValidatePassword.PASSWORD_NUMBER_PATTERN.matcher(password).matches() -> Pair(false, "One digit is required")
            else -> Pair(true,"")
        }
    }
    fun calculateTotalPrice(cartProducts: List<CartProduct>) : String {
        var totalPrice = 0.00f
        cartProducts.forEach {
            it.product.apply {
                val price = if (offerPercentage == null) price else ((price/100)*(100-offerPercentage))
                totalPrice += price*it.quantity
            }
        }
        return totalPrice.toString()
    }

}