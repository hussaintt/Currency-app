package com.hussaintt55.currencyapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.hussaintt55.currencyapp.R



class CurrencyConversionFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    var myView:View?=null
    var details:Button?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_currency_conversion, container, false)
        details = myView?.findViewById(R.id.button)
        details?.setOnClickListener {
            navigateToDetails()
        }
        return myView
    }

    fun navigateToDetails(){
        findNavController().navigate(R.id.action_currencyConversionFragment_to_historicalFragment)
    }
}