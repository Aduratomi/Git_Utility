package com.example.gitutility.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * CalculatorViewModel handles the math and display logic for the Calculator screen.
 * It follows the ViewModel pattern to keep the data safe when the screen rotates.
 */
class CalculatorViewModel : ViewModel() {
    /**
     * 'display' is what the user sees on the calculator screen.
     * 'mutableStateOf' tells Jetpack Compose to refresh the UI when this value changes.
     */
    var display by mutableStateOf("")
        private set

    /**
     * 'expression' is the full math formula we are building (e.g., "5+3").
     */
    private var expression = ""

    /**
     * Adds a number to the current expression.
     */
    fun appendNumber(number: String) {
        expression += number
        display = expression
    }

    /**
     * Adds an operator (+, -, *, /) if the last character isn't already an operator.
     */
    fun appendOperator(operator: String) {
        if (expression.isNotEmpty() && !expression.endsWith("+") &&
            !expression.endsWith("-") && !expression.endsWith("*") &&
            !expression.endsWith("/") && !expression.endsWith(".")) {
            expression += operator
            display = expression
        }
    }

    /**
     * Deletes the last character typed.
     */
    fun backspace() {
        if (expression.isNotEmpty()) {
            expression = expression.dropLast(1)
            display = expression
        }
    }

    /**
     * Solves the math formula and updates the display with the result.
     */
    fun calculate() {


        try {
            val result = eval(expression)
            display = result.toString()
            // Reset the expression to the result so the user can keep calculating
            expression = result.toString()
        } catch (e: Exception) {
            display = "Error"
            expression = ""
        }
    }

    /**
     * Clears everything to start fresh.
     */
    fun clear() {
        expression = ""
        display = ""
    }

    /**
     * A helper function that parses and calculates the math string.
     * It handles multiplication/division before addition/subtraction automatically.
     */
    private fun eval(expr: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '

            // Moves to the next character in the formula
            fun nextChar() {
                ch = if (++pos < expr.length) expr[pos] else '\u0000'
            }

            // Checks if the current character matches what we expect and moves forward
            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            // Starts the parsing process
            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expr.length) throw RuntimeException("Unexpected: " + ch)
                return x
            }

            // Logic for addition and subtraction
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    when {
                        eat('+') -> x += parseTerm()
                        eat('-') -> x -= parseTerm()
                        else -> return x
                    }
                }
            }

            // Logic for multiplication and division
            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    when {
                        eat('*') -> x *= parseFactor()
                        eat('/') -> x /= parseFactor()
                        else -> return x
                    }
                }
            }

            // Logic for numbers, decimals, and parentheses
            fun parseFactor(): Double {
                if (eat('+')) return parseFactor()
                if (eat('-')) return -parseFactor()
                var x: Double
                val startPos = this.pos
                if (eat('(')) {
                    x = parseExpression()
                    eat(')')
                } else if (ch in '0'..'9' || ch == '.') {
                    while (ch in '0'..'9' || ch == '.') nextChar()
                    x = expr.substring(startPos, this.pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: " + ch)
                }
                return x
            }
        }.parse()
    }
}
