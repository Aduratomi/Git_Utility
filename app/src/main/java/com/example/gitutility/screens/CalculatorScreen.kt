package com.example.gitutility.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gitutility.components.CalculatorButton
import com.example.gitutility.components.GlassCard
import com.example.gitutility.viewmodel.CalculatorViewModel

/**
 * CalculatorScreen provides the user interface for our calculator.
 * It uses an adaptive layout to work well on both portrait and landscape.
 */
@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel = viewModel()) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
            // In landscape, we put the display and buttons side-by-side
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    DisplayBox(display = viewModel.display)
                }

                Column(
                    modifier = Modifier
                        .weight(1.5f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CalculatorButtonsGrid(viewModel)
                }
            }
        } else {
            // In portrait, the display is on top and buttons are below
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                DisplayBox(display = viewModel.display)
                CalculatorButtonsGrid(viewModel)
            }
        }
    }
}

/**
 * Reusable component for the calculator's numeric display.
 */
@Composable
fun DisplayBox(display: String) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.CenterEnd // Align numbers to the right
        ) {
            Text(
                text = display.ifEmpty { "0" }, // Show 0 if the formula is empty
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * The grid of buttons for the calculator.
 */
@Composable
fun CalculatorButtonsGrid(viewModel: CalculatorViewModel) {
    // Define the layout of our buttons
    val buttons = listOf(
        listOf("C", "⌫", "/", "×"),
        listOf("7", "8", "9", "−"),
        listOf("4", "5", "6", "+"),
        listOf("1", "2", "3", "."),
        listOf("0", "0", "=")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { btn ->
                    // 0 and = buttons take up more space (weight)
                    val weight = if (btn == "0" || btn == "=") 0.5f else 0.25f

                    CalculatorButton(
                        value = btn,
                        onClick = {
                            // Link each button to its logic in the ViewModel
                            when (btn) {
                                "=" -> viewModel.calculate()
                                "C" -> viewModel.clear()
                                "⌫" -> viewModel.backspace()
                                "×" -> viewModel.appendOperator("*")
                                "−" -> viewModel.appendOperator("-")
                                in listOf("+", "/", ".") -> viewModel.appendOperator(btn)
                                else -> viewModel.appendNumber(btn)
                            }
                        },
                        modifier = Modifier
                            .weight(weight)
                            .padding(vertical = 4.dp),
                        isOperator = btn in listOf("×", "−", "+", "/", "."),
                        isClear = btn in listOf("C", "⌫"),
                        isEquals = btn == "="
                    )
                }
            }
        }
    }
}
