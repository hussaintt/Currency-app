package com.hussaintt55.currencyapp.model.CurrencyList


import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val info: String,
    @SerializedName("type")
    val type: String
)