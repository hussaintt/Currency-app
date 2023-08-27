package com.hussaintt55.currencyapp.ui.brain

import android.app.Activity
import android.app.AlertDialog

object brain {
    fun calculateCurrentValue(Value: Double, currency1: Double, currency2: Double): Double {
        return (Value * (currency1 / currency2))
    }
    fun roundToDecimalPlaces(number: Double, decimalPlaces: Int): Double {
        val factor = Math.pow(10.0, decimalPlaces.toDouble())
        return Math.round(number * factor) / factor
    }

    fun ShowDialog(activity:Activity,message:String){
        val alertDialogBuilder = AlertDialog.Builder(activity)
        // Set the dialog title and message
        alertDialogBuilder.setTitle("Error Happened")
        alertDialogBuilder.setMessage(message)
        // Add a positive button and define its action
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            // Handle the OK button click
            dialog.dismiss() // Close the dialog
        }
        // Create the AlertDialog
        val alertDialog = alertDialogBuilder.create()
        // Show the AlertDialog
        alertDialog.show()

}
}