package com.example.gitutility.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gitutility.data.models.UnitConversions
import com.example.gitutility.data.models.UnitType

/**
 * UnitConverterViewModel handles conversions for physical units like length, weight, and volume.
 */
class UnitConverterViewModel : ViewModel() {
    // Current category (Length, Weight, or Volume)
    var unitType by mutableStateOf<UnitType>(UnitType.Length)
        private set

    // The number the user typed in
    var inputValue by mutableStateOf("")
        private set

    // The calculated result
    var outputValue by mutableStateOf("")
        private set

    // The unit the user is converting from (e.g., meters)
    var fromUnit by mutableStateOf("m")
        private set

    // The unit the user is converting to (e.g., kilometers)
    var toUnit by mutableStateOf("km")
        private set

    /**
     * Changes the category (e.g., from Length to Weight) and resets the selected units.
     */
    fun updateUnitType(type: UnitType) {
        unitType = type
        fromUnit = type.units[0].id
        toUnit = if (type.units.size > 1) type.units[1].id else type.units[0].id
        convert()
    }

    /**
     * Updates input amount and recalculates result.
     */
    fun updateInputValue(value: String) {
        inputValue = value
        convert()
    }

    /**
     * Changes source unit and recalculates.
     */
    fun updateFromUnit(unit: String) {
        fromUnit = unit
        convert()
    }

    /**
     * Changes target unit and recalculates.
     */
    fun updateToUnit(unit: String) {
        toUnit = unit
        convert()
    }

    /**
     * Swaps 'From' and 'To' units.
     */
    fun swap() {
        val temp = fromUnit
        fromUnit = toUnit
        toUnit = temp
        convert()
    }

    /**
     * Uses our static UnitConversions utility to do the math.
     */
    private fun convert() {
        outputValue = if (inputValue.isEmpty()) {
            ""
        } else {
            try {
                val value = inputValue.toDouble()
                val result = UnitConversions.convert(value, fromUnit, toUnit, unitType)
                // Show more decimals (6) because unit differences can be very small
                String.format("%.6f", result)
            } catch (e: Exception) {
                ""
            }
        }
    }
}
