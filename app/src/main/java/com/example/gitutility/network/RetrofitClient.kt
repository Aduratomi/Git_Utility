package com.example.gitutility.network

import com.example.gitutility.data.api.ExchangeRateApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.exchangerate-api.com/v4/latest/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()

    val exchangeRateApi: ExchangeRateApi = retrofit.create(ExchangeRateApi::class.java)
}
