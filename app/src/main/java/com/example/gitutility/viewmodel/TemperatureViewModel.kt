package com.example.gitutility.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TemperatureViewModel : ViewModel() {
    var inputValue by mutableStateOf("")
        private set

    var outputValue by mutableStateOf("")
        private set

    var fromTemp by mutableStateOf("C")
        private set

    var toTemp by mutableStateOf("F")
        private set

    fun updateInputValue(value: String) {
        inputValue = value
        convert()
    }

    fun updateFromTemp(temp: String) {
        fromTemp = temp
        convert()
    }

    fun updateToTemp(temp: String) {
        toTemp = temp
        convert()
    }

    fun swap() {
        val tempFrom = fromTemp
        fromTemp = toTemp
        toTemp = tempFrom
        convert()
    }

    private fun convert() {
        outputValue = if (inputValue.isEmpty()) {
            ""
        } else {
            try {
                val value = inputValue.toDouble()

                val celsius = when (fromTemp) {
                    "C" -> value
                    "F" -> (value - 32) * 5 / 9
                    "K" -> value - 273.15
                    else -> value
                }

                val result = when (toTemp) {
                    "C" -> celsius
                    "F" -> (celsius * 9 / 5) + 32
                    "K" -> celsius + 273.15
                    else -> celsius
                }

                String.format("%.2f", result)
            } catch (e: Exception) {
                ""
            }
        }
    }
}
