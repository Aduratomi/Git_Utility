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

/**
 * CurrencyUiState defines all the data the UI needs to show for the Currency Converter screen.
 */
data class CurrencyUiState(
    val isLoading: Boolean = true,           // Shows a loading spinner while fetching data
    val error: String? = null,                // Holds an error message if something goes wrong
    val lastUpdated: String = "Loading...",   // When the exchange rates were last refreshed
    val isRealTime: Boolean = false,          // True if using live data, false if using offline backup
    val rates: Map<String, Double> = emptyMap(), // The actual exchange rate numbers
    val availableCurrencies: List<Currency> = emptyList() // The list of currencies the user can choose from
)

/**
 * CurrencyConverterViewModel handles the brain-work for the Currency Converter screen.
 */
class CurrencyConverterViewModel : ViewModel() {
    // The repository is like a waiter that goes to the "internet kitchen" to get our data
    private val repository = CurrencyRepository()

    // Holds the amount typed by the user (e.g., "100")
    var inputValue by mutableStateOf("")
        private set

    // Holds the result of the conversion (e.g., "85.50")
    var outputValue by mutableStateOf("")
        private set

    // The currency we are converting FROM (default is USD)
    var fromCurrency by mutableStateOf("USD")
        private set

    // The currency we are converting TO (default is EUR)
    var toCurrency by mutableStateOf("EUR")
        private set

    // Private state for updating data internally, public state for the UI to "read"
    private val _uiState = MutableStateFlow(CurrencyUiState())
    val uiState: StateFlow<CurrencyUiState> = _uiState.asStateFlow()

    // init runs as soon as the ViewModel is created
    init {
        fetchExchangeRates()
    }

    /**
     * fetchExchangeRates asks the repository to get the latest numbers from the internet.
     */
    private fun fetchExchangeRates() {
        // viewModelScope ensures this task stops if the user leaves the screen
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // Try to get data starting with our 'fromCurrency'
                val response = repository.getExchangeRates(fromCurrency)
                if (response != null) {
                    // Create a pretty date/time string
                    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                    val timestamp = formatter.format(Date())

                    // Map the codes (like "EUR") into full Currency objects (like "Euro")
                    val currencyList = response.rates.keys.map { code ->
                        CurrencyData.fromCode(code)
                    }.sortedBy { it.code }

                    // Update the UI state with all the new info
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        lastUpdated = timestamp,
                        isRealTime = true,
                        rates = response.rates,
                        availableCurrencies = currencyList
                    )
                    // Do the math calculation with the new rates
                    convert()
                } else {
                    throw Exception("Failed to fetch rates")
                }
            } catch (e: Exception) {
                // If there's an error (like no WiFi), let the user know
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Network error. Using cached rates.",
                    isRealTime = false
                )
            }
        }
    }

    /**
     * Updates the input amount and recalculates the result.
     */
    fun updateInputValue(value: String) {
        inputValue = value
        convert()
    }

    /**
     * Changes the source currency and gets new rates.
     */
    fun updateFromCurrency(currency: String) {
        fromCurrency = currency
        fetchExchangeRates()
    }

    /**
     * Changes the target currency and recalculates.
     */
    fun updateToCurrency(currency: String) {
        toCurrency = currency
        convert()
    }

    /**
     * Swaps the "From" and "To" currencies.
     */
    fun swap() {
        val temp = fromCurrency
        fromCurrency = toCurrency
        toCurrency = temp
        fetchExchangeRates()
    }

    /**
     * The actual math logic to convert the numbers.
     */
    private fun convert() {
        val rates = _uiState.value.rates
        outputValue = if (inputValue.isEmpty()) {
            "" // Clear result if input is empty
        } else {
            try {
                val amount = inputValue.toDouble()
                val fromRate = rates[fromCurrency] ?: 1.0
                val toRate = rates[toCurrency] ?: 1.0
                
                // Formula: (Amount / Rate of base) * Rate of target
                val result = (amount / fromRate) * toRate
                // Format to 2 decimal places (e.g., 10.50)
                String.format("%.2f", result)
            } catch (e: Exception) {
                "" // If user types something weird, don't crash
            }
        }
    }
}
