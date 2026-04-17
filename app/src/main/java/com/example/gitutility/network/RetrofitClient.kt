package com.example.gitutility.network

import com.example.gitutility.data.api.ExchangeRateApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitClient is a singleton object that sets up our network connection.
 * It's responsible for talking to the internet and converting JSON data into Kotlin objects.
 */
object RetrofitClient {
    // This helper logs network requests to the console (useful for debugging)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Configure the network client with the logger
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Create the Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.exchangerate-api.com/v4/latest/") // The base web address for the API
        .addConverterFactory(GsonConverterFactory.create()) // Use Gson to handle JSON conversion
        .client(httpClient)
        .build()

    /**
     * This provides the actual interface implementation that ViewModels will use.
     */
    val exchangeRateApi: ExchangeRateApi = retrofit.create(ExchangeRateApi::class.java)
}
