package com.hussaintt55.currencyapp.model.CurrencyList


import com.google.gson.annotations.SerializedName

data class CurrencyListResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: HashMap<String,Double>,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Int
)