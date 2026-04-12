package com.example.gitutility.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gitutility.data.models.UnitConversions
import com.example.gitutility.data.models.UnitType

class UnitConverterViewModel : ViewModel() {
    var unitType by mutableStateOf<UnitType>(UnitType.Length)
        private set

    var inputValue by mutableStateOf("")
        private set

    var outputValue by mutableStateOf("")
        private set

    var fromUnit by mutableStateOf("m")
        private set

    var toUnit by mutableStateOf("km")
        private set

    fun updateUnitType(type: UnitType) {
        unitType = type
        fromUnit = type.units[0].id
        toUnit = if (type.units.size > 1) type.units[1].id else type.units[0].id
        convert()
    }

    fun updateInputValue(value: String) {
        inputValue = value
        convert()
    }

    fun updateFromUnit(unit: String) {
        fromUnit = unit
        convert()
    }

    fun updateToUnit(unit: String) {
        toUnit = unit
        convert()
    }

    fun swap() {
        val temp = fromUnit
        fromUnit = toUnit
        toUnit = temp
        convert()
    }

    private fun convert() {
        outputValue = if (inputValue.isEmpty()) {
            ""
        } else {
            try {
                val value = inputValue.toDouble()
                val result = UnitConversions.convert(value, fromUnit, toUnit, unitType)
                String.format("%.6f", result)
            } catch (e: Exception) {
                ""
            }
        }
    }
}
