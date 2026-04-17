package com.example.gitutility.data.models

import java.util.Currency as JCurrency
import java.util.Locale

/**
 * Currency represents a global currency (e.g., US Dollar).
 */
data class Currency(
    val code: String,   // ISO code like "USD"
    val name: String,   // Full name like "US Dollar"
    val symbol: String  // Symbol like "$"
)

/**
 * CurrencyData contains helper functions to work with currencies and a list of common ones.
 */
object CurrencyData {
    /**
     * Tries to create a Currency object using just the 3-letter code.
     * It uses Java's built-in Currency class to look up names and symbols.
     */
    fun fromCode(code: String): Currency {
        return try {
            val jCurrency = JCurrency.getInstance(code)
            Currency(
                code = code,
                name = jCurrency.getDisplayName(Locale.getDefault()),
                symbol = jCurrency.symbol
            )
        } catch (e: Exception) {
            // Fallback if the code is unknown
            Currency(code, code, code)
        }
    }

    // A default list of popular currencies to show before the internet data loads
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
