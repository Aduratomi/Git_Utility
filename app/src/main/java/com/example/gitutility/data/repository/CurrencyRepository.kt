package com.example.gitutility.data.repository

import com.example.gitutility.data.api.ExchangeRateResponse
import com.example.gitutility.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CurrencyRepository {
    suspend fun getExchangeRates(base: String = "USD"): ExchangeRateResponse? = withContext(Dispatchers.IO) {
        try {
            RetrofitClient.exchangeRateApi.getExchangeRates(base)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
