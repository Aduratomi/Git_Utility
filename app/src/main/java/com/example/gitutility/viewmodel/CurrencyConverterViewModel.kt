package com.example.gitutility.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitutility.data.models.Currency
import com.example.gitutility.data.models.CurrencyData
import com.example.gitutility.data.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CurrencyUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val lastUpdated: String = "Loading...",
    val isRealTime: Boolean = false,
    val rates: Map<String, Double> = emptyMap(),
    val availableCurrencies: List<Currency> = emptyList()
)

class CurrencyConverterViewModel : ViewModel() {
    private val repository = CurrencyRepository()

    var inputValue by mutableStateOf("")
        private set

    var outputValue by mutableStateOf("")
        private set

    var fromCurrency by mutableStateOf("USD")
        private set

    var toCurrency by mutableStateOf("EUR")
        private set

    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    init {
        fetchExchangeRates()
    }

    private fun fetchExchangeRates() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val response = repository.getExchangeRates(fromCurrency)
                if (response != null) {
                    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                    val timestamp = formatter.format(Date())

                    val currencyList = response.rates.keys.map { code ->
                        CurrencyData.fromCode(code)
                    }.sortedBy { it.code }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        lastUpdated = timestamp,
                        isRealTime = true,
                        rates = response.rates,
                        availableCurrencies = currencyList
                    )
                    convert()
                } else {
                    throw Exception("Failed to fetch rates")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Network error. Using cached rates.",
                    isRealTime = false
                )
            }
        }
    }

    fun updateInputValue(value: String) {
        inputValue = value
        convert()
    }

    fun updateFromCurrency(currency: String) {
        fromCurrency = currency
        fetchExchangeRates()
    }

    fun updateToCurrency(currency: String) {
        toCurrency = currency
        convert()
    }

    fun swap() {
        val temp = fromCurrency
        fromCurrency = toCurrency
        toCurrency = temp
        fetchExchangeRates()
    }

    private fun convert() {
        val rates = _uiState.value.rates
        outputValue = if (inputValue.isEmpty()) {
            ""
        } else {
            try {
                val amount = inputValue.toDouble()
                val fromRate = rates[fromCurrency] ?: 1.0
                val toRate = rates[toCurrency] ?: 1.0
                
                val result = (amount / fromRate) * toRate
                String.format("%.2f", result)
            } catch (e: Exception) {
                ""
            }
        }
    }
}
