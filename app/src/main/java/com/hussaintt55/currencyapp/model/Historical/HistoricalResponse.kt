package com.hussaintt55.currencyapp.model.Historical

import com.google.gson.annotations.SerializedName

data class HistoricalResponse(
    @SerializedName("historical")
    val historical: Boolean,
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: HashMap<String, Double>,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Int
)