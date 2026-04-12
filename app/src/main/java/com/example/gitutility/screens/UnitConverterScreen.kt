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
import com.example.gitutility.components.GlassCard
import com.example.gitutility.components.SwapCard
import com.example.gitutility.data.models.UnitType
import com.example.gitutility.viewmodel.UnitConverterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(viewModel: UnitConverterViewModel = viewModel()) {
    var typeExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            "Unit Converter",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = !typeExpanded },
                modifier = Modifier.padding(12.dp)
            ) {
                OutlinedTextField(
                    value = when (viewModel.unitType) {
                        is UnitType.Length -> "Length"
                        is UnitType.Weight -> "Weight"
                        is UnitType.Volume -> "Volume"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Unit Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false }
                ) {
                    listOf(UnitType.Length, UnitType.Weight, UnitType.Volume).forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text(when(type) {
                                    is UnitType.Length -> "Length"
                                    is UnitType.Weight -> "Weight"
                                    is UnitType.Volume -> "Volume"
                                })
                            },
                            onClick = {
                                viewModel.updateUnitType(type)
                                typeExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                ConversionCard(
                    label = "From",
                    value = viewModel.inputValue,
                    onValueChange = { viewModel.updateInputValue(it) }
                )

                UnitDropdown(
                    selectedUnit = viewModel.fromUnit,
                    units = viewModel.unitType.units,
                    onUnitChange = { viewModel.updateFromUnit(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
            }

            SwapCard { viewModel.swap() }

            Column(modifier = Modifier.weight(1f)) {
                ConversionCard(
                    label = "To",
                    value = viewModel.outputValue,
                    onValueChange = {},
                    readOnly = true
                )

                UnitDropdown(
                    selectedUnit = viewModel.toUnit,
                    units = viewModel.unitType.units,
                    onUnitChange = { viewModel.updateToUnit(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitDropdown(
    selectedUnit: String,
    units: List<com.example.gitutility.data.models.Unit>,
    onUnitChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = units.find { it.id == selectedUnit }?.symbol ?: selectedUnit,
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
            units.forEach { unit ->
                DropdownMenuItem(
                    text = { Text("${unit.label} (${unit.symbol})") },
                    onClick = {
                        onUnitChange(unit.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
