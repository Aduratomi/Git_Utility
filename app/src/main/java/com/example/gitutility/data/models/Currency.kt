package com.example.gitutility.data.models

import java.util.Currency as JCurrency
import java.util.Locale

data class Currency(
    val code: String,
    val name: String,
    val symbol: String
)

object CurrencyData {
    fun fromCode(code: String): Currency {
        return try {
            val jCurrency = JCurrency.getInstance(code)
            Currency(
                code = code,
                name = jCurrency.getDisplayName(Locale.getDefault()),
                symbol = jCurrency.symbol
            )
        } catch (e: Exception) {
            Currency(code, code, code)
        }
    }

    val currencies = listOf(
        Currency("USD", "US Dollar", "$"),
        Currency("EUR", "Euro", "€"),
        Currency("GBP", "British Pound", "£"),
        Currency("JPY", "Japanese Yen", "¥"),
        Currency("INR", "Indian Rupee", "₹"),
        Currency("AUD", "Australian Dollar", "A$"),
        Currency("CAD", "Canadian Dollar", "C$"),
        Currency("CHF", "Swiss Franc", "Fr")
    )
}
