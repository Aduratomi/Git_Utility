package com.example.gitutility.data.api

import retrofit2.http.GET
import retrofit2.http.Query

data class ExchangeRateResponse(
    val rates: Map<String, Double>,
    val base: String,
    val date: String
)

interface ExchangeRateApi {
    @GET("latest")
    suspend fun getExchangeRates(
        @Query("base") base: String = "USD"
    ): ExchangeRateResponse
}
