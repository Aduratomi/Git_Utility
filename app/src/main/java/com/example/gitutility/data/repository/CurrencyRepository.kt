package com.example.gitutility.data.repository

import com.example.gitutility.data.api.ExchangeRateResponse
import com.example.gitutility.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * CurrencyRepository handles fetching exchange rates from the network.
 */
class CurrencyRepository {
    /**
     * Fetches rates for a specific 'base' currency (e.g., USD).
     * Returns null if the request fails.
     */
    suspend fun getExchangeRates(base: String = "USD"): ExchangeRateResponse? = withContext(Dispatchers.IO) {
        // Dispatchers.IO is for network and disk operations
        try {
            RetrofitClient.exchangeRateApi.getExchangeRates(base)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
