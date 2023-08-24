package com.hussaintt55.currencyapp.model.CurrencyList

data class CurrencyListResponse(
    val base: String,
    val date: String,
    val rates: Rates,
    val success: Boolean,
    val timestamp: Int
)