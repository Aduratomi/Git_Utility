package com.example.gitutility.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * CalculatorButton is a custom button for the calculator grid.
 * It changes its color based on its role (Operator, Clear, or Number).
 */
@Composable
fun CalculatorButton(
    value: String,           // The text to show on the button (e.g., "7" or "+")
    onClick: () -> Unit,     // Function to run when the button is clicked
    modifier: Modifier = Modifier,
    isOperator: Boolean = false, // True for +, -, *, /
    isClear: Boolean = false,    // True for C and backspace
    isEquals: Boolean = false    // True for =
) {
    Box(
        modifier = modifier
            .background(
                color = when {
                    isEquals -> Color(0xFF4CAF50) // Green for result
                    isClear -> Color(0xFFf44336)  // Red for reset
                    isOperator -> MaterialTheme.colorScheme.primary // Brand color for operators
                    else -> Color(0xFFEEEEEE)     // Gray for numbers
                },
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            value,
            fontSize = 20.sp,
            color = when {
                // Use white text for colored buttons, black for gray ones
                isEquals || isClear || isOperator -> Color.White
                else -> Color.Black
            }
        )
    }
}
