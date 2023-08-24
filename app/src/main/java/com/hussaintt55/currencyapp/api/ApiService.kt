package com.hussaintt55.currencyapp.api

import com.hussaintt55.currencyapp.model.CurrencyList.CurrencyListResponse
import com.hussaintt55.currencyapp.model.Historical.HistoricalResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/latest")
    suspend fun getCurrencyList(
        @Query("access_key") apiKey:String
    ): CurrencyListResponse

    @GET("/{date}")
    suspend fun getHistoricalCurrency(
        @Path("date") date:String,
        @Query("access_key") apiKey:String
    ):HistoricalResponse
}