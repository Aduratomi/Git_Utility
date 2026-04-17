package com.example.gitutility.data.api

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * ExchangeRateResponse is the data structure for the JSON received from the API.
 */
data class ExchangeRateResponse(
    val rates: Map<String, Double>, // e.g., "EUR": 0.85
    val base: String,               // e.g., "USD"
    val date: String                // e.g., "2024-04-10"
)

/**
 * ExchangeRateApi defines the network "endpoints" we can call using Retrofit.
 */
interface ExchangeRateApi {
    /**
     * @GET indicates this is a standard web GET request.
     * @Query adds a parameter to the URL (e.g., ?base=USD).
     */
    @GET("latest")
    suspend fun getExchangeRates(
        @Query("base") base: String = "USD"
    ): ExchangeRateResponse
}
