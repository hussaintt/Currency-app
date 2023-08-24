package com.hussaintt55.currencyapp.model.Historical

data class HistoricalResponse(
    val base: String,
    val date: String,
    val historical: Boolean,
    val rates: Rates,
    val success: Boolean,
    val timestamp: Int
)