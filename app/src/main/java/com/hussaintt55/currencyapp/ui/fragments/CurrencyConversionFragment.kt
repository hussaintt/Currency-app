package com.hussaintt55.currencyapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hussaintt55.currencyapp.R
import com.hussaintt55.currencyapp.ui.MainActivityViewModel


class CurrencyConversionFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    var myView:View?=null
    var details:Button?=null

    lateinit var viewModel: MainActivityViewModel

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
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.currencyListResponse.observe(viewLifecycleOwner) {
        myHashMap=it.rates
        setupSpinner()
        }
        viewModel.Trigger(true)
        return myView
    }

    var myHashMap:HashMap<String,Double>?=null
    fun setupSpinner(){
        val spinner = myView?.findViewById<Spinner>(R.id.spinner)
        val spinner2 = myView?.findViewById<Spinner>(R.id.spinner2)
        if (spinner != null) {
            initSpinner(spinner,1)
        }
        if (spinner2 != null) {
            initSpinner(spinner2,2)
        }
    }
    fun navigateToDetails(){
        findNavController().navigate(R.id.action_currencyConversionFragment_to_historicalFragment)
    }

    fun initSpinner(spinner: Spinner,state:Int){
        myHashMap.let {
            val adapter = ArrayAdapter(requireContext(), R.layout.my_custom_spinner_item, myHashMap!!.keys.toList())
            adapter.setDropDownViewResource(R.layout.my_custom_spinner_item)
            spinner?.adapter = adapter
            spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedItem = spinner?.selectedItem.toString() // This is the selected item label
                    val selectedValue = myHashMap!![selectedItem] // This is the corresponding value
                    if (state == 1){
                        println("test123 $selectedValue from")
                    }
                    else if (state ==2){
                        println("test123 $selectedValue to")

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected, if necessary
                }
            }
        }
    }
}