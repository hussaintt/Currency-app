package com.hussaintt55.currencyapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hussaintt55.currencyapp.R
import com.hussaintt55.currencyapp.model.Historical.HistoricalResponse
import com.hussaintt55.currencyapp.model.Historical.Rates
import com.hussaintt55.currencyapp.ui.MyViewModel
import com.hussaintt55.currencyapp.ui.brain.brain
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.log


class HistoricalFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        var counter = 0
        observer = Observer<Result<HistoricalResponse>> { result ->
            val resultSccess = result.isSuccess
            if (resultSccess){
                if (result.getOrNull()?.success!!){
                    Log.d("Debug", "onCreate: receive data "+result.getOrNull())
                    Log.d("Debug", "onCreate: "+counter)
                    //fill day1 details and call day2 query
                    if (counter ==1){
                        fillDay1(result.getOrNull())
                        counter++
                        viewModel.fetchDataHistorical(getPreviousDate(2))
                    }
                    //fill day2 details and call day3 query
                    else if (counter == 2){
                        fillDay2(result.getOrNull())
                        counter++
                        viewModel.fetchDataHistorical(getPreviousDate(3))
                    }
                    //fill day3 details
                    else if(counter == 3){
                        fillDay3(result.getOrNull())
                        counter++
                    }
                }else{
                    println("debug: " +result.getOrNull()?.error?.info)
                }

            }else{
                println( "Debug: "+result.exceptionOrNull())
            }
        }
        observer?.let {
            viewModel.dataHistorical.observe(requireActivity(),observer!!)
        }
        //get day 1
        if (counter==0){
        viewModel.fetchDataHistorical(getPreviousDate(1))
        counter++
        }
    }
    lateinit var viewModel: MyViewModel
    lateinit var myView: View
    var observer:Observer<Result<HistoricalResponse>>?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView =  inflater.inflate(R.layout.fragment_historical, container, false)
        initViews()
        return myView
    }

    private fun fillDay3(data: HistoricalResponse?) {
        data?.let {
            Day3?.text = data.date
            firstCurrencyTextD3?.text = viewModel.Currency2SelectedKey.value.toString()
            secondCurrencyTextD3?.text = viewModel.Currency1SelectedKey.value.toString()
            firstCurrencyValueD3?.text = viewModel.desierdAmount.value.toString()
            secondCurrencyValueD3?.let { data?.rates?.let { it1 -> updateValues(it, it1) } }
        }
    }

    private fun fillDay2(data: HistoricalResponse?) {
        Day2?.text = data?.date
        firstCurrencyTextD2?.text = viewModel.Currency2SelectedKey.value.toString()
        secondCurrencyTextD2?.text = viewModel.Currency1SelectedKey.value.toString()
        firstCurrencyValueD2?.text = viewModel.desierdAmount.value.toString()
        secondCurrencyValueD2?.let { data?.rates?.let { it1 -> updateValues(it, it1) } }
    }

    private fun fillDay1(data: HistoricalResponse?) {
        Day1?.text = data?.date
        firstCurrencyTextD1?.text = viewModel.Currency2SelectedKey.value.toString()
        secondCurrencyTextD1?.text = viewModel.Currency1SelectedKey.value.toString()
        firstCurrencyValueD1?.text = viewModel.desierdAmount.value.toString()
        secondCurrencyValueD1?.let { data?.rates?.let { it1 -> updateValues(it, it1) } }
    }

    fun getPreviousDate(day:Long):String{
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        // Get the date of three days ago
        val threeDaysAgo = today.minusDays(day)
        // Format the date in the desired format
        val formattedDate = threeDaysAgo.format(formatter)
        return formattedDate
    }

    override fun onStop() {
        super.onStop()
        observer?.let { viewModel.dataHistorical.removeObserver(it) }
    }

    override fun onResume() {
        super.onResume()
    }
    fun initViews(){
         Day1=myView?.findViewById(R.id.date)
         Day2=myView?.findViewById(R.id.date2)
         Day3=myView?.findViewById(R.id.date3)


         firstCurrencyTextD1=myView?.findViewById(R.id.first_currency)
         firstCurrencyTextD2=myView?.findViewById(R.id.first_currency2)
         firstCurrencyTextD3=myView?.findViewById(R.id.first_currency3)


         secondCurrencyTextD1=myView?.findViewById(R.id.second_currency)
         secondCurrencyTextD2=myView?.findViewById(R.id.second_currency2)
         secondCurrencyTextD3=myView?.findViewById(R.id.second_currency3)

         firstCurrencyValueD1=myView?.findViewById(R.id.fromValue)
         firstCurrencyValueD2=myView?.findViewById(R.id.fromValue2)
         firstCurrencyValueD3=myView?.findViewById(R.id.fromValue3)


         secondCurrencyValueD1=myView?.findViewById(R.id.ToValue)
         secondCurrencyValueD2=myView?.findViewById(R.id.ToValue2)
         secondCurrencyValueD3=myView?.findViewById(R.id.ToValue3)


    }
    private fun updateValues(textView: TextView,rates: HashMap<String,Double>) {
        val v1 = viewModel.desierdAmount.value
        val c1=rates.get(viewModel.Currency1SelectedKey.value)
        val c2=rates.get(viewModel.Currency2SelectedKey.value)
        if (v1!=null&&c1!=null&&c2!=null){
            val value =(brain.calculateCurrentValue(v1, c1, c2))
            textView?.text = brain.roundToDecimalPlaces(value, 5).toString()
        }
    }

    var Day1: TextView?=null
    var Day2: TextView?=null
    var Day3: TextView?=null

    var firstCurrencyTextD1: TextView?=null
    var firstCurrencyTextD2: TextView?=null
    var firstCurrencyTextD3: TextView?=null

    var secondCurrencyTextD1: TextView?=null
    var secondCurrencyTextD2: TextView?=null
    var secondCurrencyTextD3: TextView?=null

    var firstCurrencyValueD1: TextView?=null
    var firstCurrencyValueD2: TextView?=null
    var firstCurrencyValueD3: TextView?=null

    var secondCurrencyValueD1: TextView?=null
    var secondCurrencyValueD2: TextView?=null
    var secondCurrencyValueD3: TextView?=null

}