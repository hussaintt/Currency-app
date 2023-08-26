package com.hussaintt55.currencyapp.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import java.lang.Math.log
import java.lang.Math.round


class CurrencyConversionFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        if (viewModel.myHashMap.value==null){
        viewModel.data.observe(requireActivity()) { result ->
            val resultSccess = result.isSuccess
            if (resultSccess){
                if (result.getOrNull()?.success!!){
                    Log.d("Debug", "onCreate: receive data")
                    viewModel.myHashMap.value = result.getOrNull()?.rates
                    setupSpinner()
                    startWithKnownCurrencies()
                }else{
                    println("debug: " +result.getOrNull()?.error?.info)
                }

            }else{
                println( "Debug: "+result.exceptionOrNull())
            }
        }
        }
        viewModel.fetchData()
    }

    var spinner:Spinner?=null
    var spinner2:Spinner?=null
    var myView:View?=null
    var details:Button?=null
    var firstCurrencyText:EditText?=null
    var secondCurrencyText:TextView?=null


    var reverse:ImageView?=null
    lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_currency_conversion, container, false)
        setupViews()
        return myView
    }

    fun setupSpinner(){
         initSpinner()
         initEditText()
         setupReverse()
    }
    private fun setupViews(){
        spinner = myView?.findViewById<Spinner>(R.id.spinner)
        spinner2 = myView?.findViewById<Spinner>(R.id.spinner2)
        firstCurrencyText = myView?.findViewById(R.id.fromValue)
        secondCurrencyText = myView?.findViewById(R.id.ToValue)
        reverse = myView?.findViewById(R.id.reverse)
        details = myView?.findViewById(R.id.button)
        details?.setOnClickListener {
            navigateToDetails()
        }
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
                    viewModel.desierdAmount.value = s.toString().toDouble()
                    updateValues()
                }
            }
        })
    }

    fun navigateToDetails(){
        findNavController().navigate(R.id.action_currencyConversionFragment_to_historicalFragment)
    }

    fun initSpinner(){
        viewModel.myHashMap.let {
            val adapter = ArrayAdapter(requireContext(), R.layout.my_custom_spinner_item, it.value!!.keys.toList())
            adapter.setDropDownViewResource(R.layout.my_custom_spinner_item)
            spinner?.adapter = adapter
            spinner2?.adapter = adapter
            spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.Currency1SelectedKey.value = spinner?.selectedItem.toString() // This is the selected item label
                    viewModel.Currency1SelectedValue.value = it.value!![viewModel.Currency1SelectedKey.value]!! // This is the corresponding value
                    viewModel.Currency1Possition.value = spinner?.selectedItemPosition!!
                    updateValues()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected, if necessary
                }
            }

            spinner2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                     viewModel.Currency2SelectedKey.value = spinner2?.selectedItem.toString() // This is the selected item label
                     viewModel.Currency2SelectedValue.value = viewModel.myHashMap.value!![viewModel.Currency2SelectedKey.value]!! // This is the corresponding value
                     viewModel.Currency2Possition.value = spinner2?.selectedItemPosition!!
                     updateValues()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case when nothing is selected, if necessary
                }
            }
        }
    }

    private fun updateValues() {
        val v1 = viewModel.desierdAmount.value
        val c1=viewModel.Currency1SelectedValue.value
        val c2 = viewModel.Currency2SelectedValue.value
        if (v1!=null&&c1!=null&&c2!=null){
        val value =(calculateCurrentValue(v1,c1,c2))
        secondCurrencyText?.text =roundToDecimalPlaces(value,5).toString()
        }
    }

    fun calculateCurrentValue(Value: Double, currency1: Double, currency2: Double): Double {
        return (Value * (currency1 / currency2))
    }

    fun reverseValues(){
        val temp1 = viewModel.Currency1Possition.value
        val temp2 = viewModel.Currency2Possition.value
        temp2?.let {spinner?.setSelection(it) }
        temp1?.let {spinner2?.setSelection(temp1)  }
        updateValues()
    }

    fun roundToDecimalPlaces(number: Double, decimalPlaces: Int): Double {
        val factor = Math.pow(10.0, decimalPlaces.toDouble())
        return round(number * factor) / factor
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("Debug", "onResume: ")
        fillValuesOnResume()
    }

    private fun fillValuesOnResume(){
        if (viewModel.myHashMap.value!=null){
            setupSpinner()
            viewModel.Currency1Possition.value?.let { spinner?.setSelection(it) }
            viewModel.Currency2Possition.value?.let { spinner2?.setSelection(it) }
            viewModel.desierdAmount.value?.let { firstCurrencyText?.text }
            updateValues()
        }
    }
    private fun startWithKnownCurrencies(){
        viewModel.desierdAmount.value = 1.0
        val arrayofCurrencies: MutableSet<String>? = viewModel.myHashMap.value?.keys
        arrayofCurrencies?.indexOf("EGP")?.let {spinner?.setSelection(it) }
        arrayofCurrencies?.indexOf("USD")?.let { spinner2?.setSelection(it) }
    }
}