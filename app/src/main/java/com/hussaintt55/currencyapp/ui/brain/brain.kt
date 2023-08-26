package com.hussaintt55.currencyapp.ui.brain

object brain {
    fun calculateCurrentValue(Value: Double, currency1: Double, currency2: Double): Double {
        return (Value * (currency1 / currency2))
    }
    fun roundToDecimalPlaces(number: Double, decimalPlaces: Int): Double {
        val factor = Math.pow(10.0, decimalPlaces.toDouble())
        return Math.round(number * factor) / factor
    }
}