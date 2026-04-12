package com.example.gitutility.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    var display by mutableStateOf("")
        private set

    private var expression = ""

    fun appendNumber(number: String) {
        expression += number
        display = expression
    }

    fun appendOperator(operator: String) {
        if (expression.isNotEmpty() && !expression.endsWith("+") &&
            !expression.endsWith("-") && !expression.endsWith("*") &&
            !expression.endsWith("/") && !expression.endsWith(".")) {
            expression += operator
            display = expression
        }
    }

    fun backspace() {
        if (expression.isNotEmpty()) {
            expression = expression.dropLast(1)
            display = expression
        }
    }

    fun calculate() {
        try {
            val result = eval(expression)
            display = result.toString()
            expression = result.toString()
        } catch (e: Exception) {
            display = "Error"
            expression = ""
        }
    }

    fun clear() {
        expression = ""
        display = ""
    }

    private fun eval(expr: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '

            fun nextChar() {
                ch = if (++pos < expr.length) expr[pos] else '\u0000'
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch != charToEat) return false
                nextChar()
                return true
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expr.length) throw RuntimeException("Unexpected: " + ch)
                return x
            }

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

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor()
                if (eat('-')) return -parseFactor()
                var x: Double
                val startPos = this.pos
                if (eat('(')) {
                    x = parseExpression()
                    eat(')')
                } else if (ch >= '0' && ch <= '9' || ch == '.') {
                    while (ch >= '0' && ch <= '9' || ch == '.') nextChar()
                    x = expr.substring(startPos, this.pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: " + ch)
                }
                return x
            }
        }.parse()
    }
}
