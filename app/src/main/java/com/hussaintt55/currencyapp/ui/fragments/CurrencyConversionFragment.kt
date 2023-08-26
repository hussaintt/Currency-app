package com.hussaintt55.currencyapp.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hussaintt55.currencyapp.R
import com.hussaintt55.currencyapp.ui.MyViewModel
import java.lang.Math.round


class CurrencyConversionFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    var myHashMap:HashMap<String,Double>?=null
    var spinner:Spinner?=null
    var spinner2:Spinner?=null
    var myView:View?=null
    var details:Button?=null
    var firstCurrencyText:EditText?=null
    var secondCurrencyText:TextView?=null
    var desierdAmount = 1.0
    var Currency1SelectedValue = 0.0
    var Currency2SelectedValue = 0.0
    var Currency1SelectedKey = ""
    var Currency2SelectedKey = ""
    var Currency1Possition = 2
    var Currency2Possition = 1
    var reverse:ImageView?=null
    lateinit var viewModel: MyViewModel

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
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        viewModel.data.observe(viewLifecycleOwner) { result ->
            val resultSccess = result.isSuccess
            if (resultSccess){
                if (result.getOrNull()?.success!!){
                    myHashMap = result.getOrNull()?.rates
                    setupSpinner()
                }else{
                    println("debug: " +result.getOrNull()?.error?.info)
                }

            }else{
                println( "Debug: "+result.exceptionOrNull())
            }
        }
        viewModel.fetchData()
        return myView
    }

    fun setupSpinner(){
         spinner = myView?.findViewById<Spinner>(R.id.spinner)
         spinner2 = myView?.findViewById<Spinner>(R.id.spinner2)
         firstCurrencyText = myView?.findViewById(R.id.fromValue)
         secondCurrencyText = myView?.findViewById(R.id.ToValue)
         reverse = myView?.findViewById(R.id.reverse)
        initSpinner()
        initEditText()
        setupReverse()
    }

    private fun setupReverse() {
        reverse?.setOnClickListener {
            reverseValues()
        }
    }

    private fun initEditText() {
        firstCurrencyText?.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) { }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    desierdAmount = s.toString().toDouble()
                    updateValues()
                }
            }
        })
    }

    fun navigateToDetails(){
        findNavController().navigate(R.id.action_currencyConversionFragment_to_historicalFragment)
    }

    fun initSpinner(){
        myHashMap.let {
            val adapter = ArrayAdapter(requireContext(), R.layout.my_custom_spinner_item, myHashMap!!.keys.toList())
            adapter.setDropDownViewResource(R.layout.my_custom_spinner_item)
            spinner?.adapter = adapter
            spinner2?.adapter = adapter
            spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Currency1SelectedKey = spinner?.selectedItem.toString() // This is the selected item label
                    Currency1SelectedValue = myHashMap!![Currency1SelectedKey]!! // This is the corresponding value
                    Currency1Possition = spinner?.selectedItemPosition!!
                    updateValues()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected, if necessary
                }
            }

            spinner2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                     Currency2SelectedKey = spinner2?.selectedItem.toString() // This is the selected item label
                     Currency2SelectedValue = myHashMap!![Currency2SelectedKey]!! // This is the corresponding value
                     Currency2Possition = spinner2?.selectedItemPosition!!
                     updateValues()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected, if necessary
                }
            }
        }
    }

    private fun updateValues() {
        val value =(calculateCurrentValue(desierdAmount, Currency1SelectedValue, Currency2SelectedValue))
        secondCurrencyText?.text =roundToDecimalPlaces(value,5).toString()
    }

    fun calculateCurrentValue(Value: Double, currency1: Double, currency2: Double): Double {
        return (Value * (currency1 / currency2))
    }

    fun reverseValues(){
        val temp1 = Currency1Possition
        val temp2 = Currency2Possition
        spinner?.setSelection(temp2)
        spinner2?.setSelection(temp1)
        updateValues()
    }

    fun roundToDecimalPlaces(number: Double, decimalPlaces: Int): Double {
        val factor = Math.pow(10.0, decimalPlaces.toDouble())
        return round(number * factor) / factor
    }
}