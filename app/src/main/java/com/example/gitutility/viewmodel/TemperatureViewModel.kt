package com.example.gitutility.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * TemperatureViewModel manages the logic for converting temperatures
 * between Celsius, Fahrenheit, and Kelvin.
 */
class TemperatureViewModel : ViewModel() {
    // The amount typed by the user (as a string)
    var inputValue by mutableStateOf("")
        private set

    // The calculated result
    var outputValue by mutableStateOf("")
        private set

    // The unit we are converting from (default: Celsius)
    var fromTemp by mutableStateOf("C")
        private set

    // The unit we are converting to (default: Fahrenheit)
    var toTemp by mutableStateOf("F")
        private set

    /**
     * Updates the input value and performs the conversion.
     */
    fun updateInputValue(value: String) {
        inputValue = value
        convert()
    }

    /**
     * Updates the 'from' unit and performs the conversion.
     */
    fun updateFromTemp(temp: String) {
        fromTemp = temp
        convert()
    }

    /**
     * Updates the 'to' unit and performs the conversion.
     */
    fun updateToTemp(temp: String) {
        toTemp = temp
        convert()
    }

    /**
     * Swaps the source and target units.
     */
    fun swap() {
        val tempFrom = fromTemp
        fromTemp = toTemp
        toTemp = tempFrom
        convert()
    }

    /**
     * The core conversion logic.
     * It first converts the input to Celsius, and then from Celsius to the target unit.
     */
    private fun convert() {
        outputValue = if (inputValue.isEmpty()) {
            ""
        } else {
            try {
                val value = inputValue.toDouble()

                // Step 1: Convert everything to Celsius as a common base
                val celsius = when (fromTemp) {
                    "C" -> value
                    "F" -> (value - 32) * 5 / 9
                    "K" -> value - 273.15
                    else -> value
                }

                // Step 2: Convert from Celsius to the desired unit
                val result = when (toTemp) {
                    "C" -> celsius
                    "F" -> (celsius * 9 / 5) + 32
                    "K" -> celsius + 273.15
                    else -> celsius
                }

                // Show the result rounded to 2 decimal places
                String.format("%.2f", result)
            } catch (e: Exception) {
                "" // Don't show anything if the input is not a valid number
            }
        }
    }
}
