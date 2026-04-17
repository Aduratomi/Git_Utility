package com.example.gitutility.data.models

/**
 * Unit is a simple data container for a measurement unit (e.g., Meter).
 */
data class Unit(
    val id: String,    // Short code like "kg"
    val label: String, // Full name like "Kilogram"
    val symbol: String // Symbol like "kg"
)

/**
 * UnitType defines categories of units (Length, Weight, Volume).
 */
sealed class UnitType(val units: List<Unit>) {
    object Length : UnitType(
        listOf(
            Unit("m", "Meter", "m"),
            Unit("km", "Kilometer", "km"),
            Unit("cm", "Centimeter", "cm"),
            Unit("mm", "Millimeter", "mm"),
            Unit("ft", "Feet", "ft"),
            Unit("in", "Inch", "in"),
            Unit("mi", "Mile", "mi")
        )
    )

    object Weight : UnitType(
        listOf(
            Unit("kg", "Kilogram", "kg"),
            Unit("g", "Gram", "g"),
            Unit("mg", "Milligram", "mg"),
            Unit("lb", "Pound", "lb"),
            Unit("oz", "Ounce", "oz")
        )
    )

    object Volume : UnitType(
        listOf(
            Unit("l", "Liter", "l"),
            Unit("ml", "Milliliter", "ml"),
            Unit("gal", "Gallon", "gal"),
            Unit("pt", "Pint", "pt"),
            Unit("cup", "Cup", "cup")
        )
    )
}

/**
 * UnitConversions contains the math factors used to calculate conversions.
 * Everything is relative to a base unit (e.g., 1 Meter, 1 Kilogram, 1 Liter).
 */
object UnitConversions {
    private val lengthConversions = mapOf(
        "m" to 1.0,
        "km" to 0.001,
        "cm" to 100.0,
        "mm" to 1000.0,
        "ft" to 3.28084,
        "in" to 39.3701,
        "mi" to 0.000621371
    )

    private val weightConversions = mapOf(
        "kg" to 1.0,
        "g" to 1000.0,
        "mg" to 1000000.0,
        "lb" to 2.20462,
        "oz" to 35.274
    )

    private val volumeConversions = mapOf(
        "l" to 1.0,
        "ml" to 1000.0,
        "gal" to 0.264172,
        "pt" to 2.11338,
        "cup" to 4.22675
    )

    /**
     * convert takes a number and its units and calculates the final result.
     */
    fun convert(value: Double, fromUnit: String, toUnit: String, type: UnitType): Double {
        // Pick the correct conversion map based on the category
        val conversions = when (type) {
            is UnitType.Length -> lengthConversions
            is UnitType.Weight -> weightConversions
            is UnitType.Volume -> volumeConversions
        }

        // Convert the input value to the base unit first (e.g., convert feet to meters)
        val baseValue = value / (conversions[fromUnit] ?: 1.0)
        // Then convert from the base unit to the final unit (e.g., convert meters to kilometers)
        return baseValue * (conversions[toUnit] ?: 1.0)
    }
}
