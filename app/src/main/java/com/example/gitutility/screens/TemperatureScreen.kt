package com.example.gitutility.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gitutility.components.ConversionCard
import com.example.gitutility.components.SwapCard
import com.example.gitutility.viewmodel.TemperatureViewModel

/**
 * TemperatureScreen allows users to convert between Celsius, Fahrenheit, and Kelvin.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureScreen(viewModel: TemperatureViewModel = viewModel()) {
    // List of units for the dropdown (Label and ID)
    val tempUnits = listOf("°C" to "C", "°F" to "F", "K" to "K")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            "Temperature Converter",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Source side
            Column(modifier = Modifier.weight(1f)) {
                ConversionCard(
                    label = "From",
                    value = viewModel.inputValue,
                    onValueChange = { viewModel.updateInputValue(it) }
                )

                TempDropdown(
                    selectedTemp = viewModel.fromTemp,
                    tempUnits = tempUnits,
                    onTempChange = { viewModel.updateFromTemp(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
            }

            // Central Swap Button
            SwapCard { viewModel.swap() }

            // Destination side
            Column(modifier = Modifier.weight(1f)) {
                ConversionCard(
                    label = "To",
                    value = viewModel.outputValue,
                    onValueChange = {},
                    readOnly = true
                )

                TempDropdown(
                    selectedTemp = viewModel.toTemp,
                    tempUnits = tempUnits,
                    onTempChange = { viewModel.updateToTemp(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
            }
        }
    }
}

/**
 * Reusable dropdown component for selecting a temperature unit.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempDropdown(
    selectedTemp: String,
    tempUnits: List<Pair<String, String>>,
    onTempChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val displayLabel = tempUnits.find { it.second == selectedTemp }?.first ?: selectedTemp

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = displayLabel,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            tempUnits.forEach { (label, code) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onTempChange(code)
                        expanded = false
                    }
                )
            }
        }
    }
}
