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

@Composable
fun CalculatorButton(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isOperator: Boolean = false,
    isClear: Boolean = false,
    isEquals: Boolean = false
) {
    Box(
        modifier = modifier
            .background(
                color = when {
                    isEquals -> Color(0xFF4CAF50)
                    isClear -> Color(0xFFf44336)
                    isOperator -> MaterialTheme.colorScheme.primary
                    else -> Color(0xFFEEEEEE)
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
                isEquals || isClear || isOperator -> Color.White
                else -> Color.Black
            }
        )
    }
}
