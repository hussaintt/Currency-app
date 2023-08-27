package com.hussaintt55.currencyapp.ui.fragments

import DataRepository
import DataRepository.popularCountries
import android.graphics.Insets
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsAnimation.Bounds
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hussaintt55.currencyapp.R
import com.hussaintt55.currencyapp.model.Historical.HistoricalResponse
import com.hussaintt55.currencyapp.model.adapter.Conversations
import com.hussaintt55.currencyapp.ui.MyViewModel
import com.hussaintt55.currencyapp.ui.adapter.OtherCurrenenciesRecycler
import com.hussaintt55.currencyapp.ui.brain.brain
import com.patrykandpatrick.vico.core.axis.Axis
import com.patrykandpatrick.vico.core.axis.AxisManager
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.AxisRenderer
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.extension.setFieldValue
import com.patrykandpatrick.vico.views.chart.ChartView
import java.time.LocalDate
import java.time.format.DateTimeFormatter



class HistoricalFragment : Fragment(), OtherCurrenenciesRecycler.ListItemClickListener {

    var OtherCurrenenciesList: ArrayList<Conversations>?=null
    var list_for_chart:ArrayList<Double>?=java.util.ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        var counter = 0
         OtherCurrenenciesList= java.util.ArrayList()
        observer = Observer<Result<HistoricalResponse>> { result ->
            val resultSccess = result.isSuccess
            if (resultSccess){
                if (result.getOrNull()?.success!!){
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
                    brain.ShowDialog(requireActivity(),result.getOrNull()?.error?.info.toString())
                }

            }else{
                brain.ShowDialog(requireActivity(),result.exceptionOrNull().toString())
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
        setAdapter()
        return myView
    }

    private fun fillDay3(data: HistoricalResponse?) {
        data?.let {
            Day3?.text = data.date
            firstCurrencyTextD3?.text = viewModel.Currency2SelectedKey.value.toString()
            secondCurrencyTextD3?.text = viewModel.Currency1SelectedKey.value.toString()
            firstCurrencyValueD3?.text = viewModel.desierdAmount.value.toString()
            secondCurrencyValueD3?.let { data.rates.let { Rates ->
                updateValues(it, Rates)
                list_for_chart?.add(brain.calculateCurrentValue(
                    viewModel.desierdAmount.value!!,
                    Rates[viewModel.Currency1SelectedKey.value!!]!!,
                    Rates[viewModel.Currency2SelectedKey.value!!]!!
                ))
                progressBar?.visibility = View.INVISIBLE
                setupChart()
            }
            }
        }
    }

    private fun fillDay2(data: HistoricalResponse?) {
        Day2?.text = data?.date
        firstCurrencyTextD2?.text = viewModel.Currency2SelectedKey.value.toString()
        secondCurrencyTextD2?.text = viewModel.Currency1SelectedKey.value.toString()
        firstCurrencyValueD2?.text = viewModel.desierdAmount.value.toString()
        secondCurrencyValueD2?.let { data?.rates?.let { Rates ->
            updateValues(it, Rates)
            list_for_chart?.add(brain.calculateCurrentValue(
                viewModel.desierdAmount.value!!,
                Rates[viewModel.Currency1SelectedKey.value!!]!!,
                Rates[viewModel.Currency2SelectedKey.value!!]!!
            ))
        } }
    }

    private fun fillDay1(data: HistoricalResponse?) {
        Day1?.text = data?.date
        firstCurrencyTextD1?.text = viewModel.Currency2SelectedKey.value.toString()
        secondCurrencyTextD1?.text = viewModel.Currency1SelectedKey.value.toString()
        firstCurrencyValueD1?.text = viewModel.desierdAmount.value.toString()
        secondCurrencyValueD1?.let {
            data?.rates?.let { Rates -> updateValues(it, Rates)
                list_for_chart?.add(brain.calculateCurrentValue(
                    viewModel.desierdAmount.value!!,
                    Rates.get(viewModel.Currency1SelectedKey.value!!)!!,
                    Rates.get(viewModel.Currency2SelectedKey.value!!)!!
                )
                )
            }

        }
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
    private fun initViews(){
         Day1= myView.findViewById(R.id.date)
         Day2= myView.findViewById(R.id.date2)
         Day3= myView.findViewById(R.id.date3)


         firstCurrencyTextD1= myView.findViewById(R.id.first_currency)
         firstCurrencyTextD2= myView.findViewById(R.id.from)
         firstCurrencyTextD3=myView.findViewById(R.id.first_currency3)


         secondCurrencyTextD1=myView.findViewById(R.id.second_currency)
         secondCurrencyTextD2=myView.findViewById(R.id.to)
         secondCurrencyTextD3=myView.findViewById(R.id.second_currency3)

         firstCurrencyValueD1=myView.findViewById(R.id.fromValue)
         firstCurrencyValueD2=myView.findViewById(R.id.fromValue2)
         firstCurrencyValueD3=myView.findViewById(R.id.fromValue3)


         secondCurrencyValueD1=myView.findViewById(R.id.ToValue)
         secondCurrencyValueD2=myView.findViewById(R.id.ToValue2)
         secondCurrencyValueD3=myView.findViewById(R.id.ToValue3)

         myRecycler1 = myView.findViewById(R.id.myRecycler)
         progressBar = myView.findViewById(R.id.progressBar)
    }
    private fun updateValues(textView: TextView,rates: HashMap<String,Double>) {
        val v1 = viewModel.desierdAmount.value
        val c1=rates.get(viewModel.Currency1SelectedKey.value)
        val c2=rates.get(viewModel.Currency2SelectedKey.value)
        if (v1!=null&&c1!=null&&c2!=null){
            val value =(brain.calculateCurrentValue(v1, c1, c2))
            textView.text = brain.roundToDecimalPlaces(value, 5).toString()
        }
    }
    private fun setAdapter(){
                fillRecyclerList()
                if (OtherCurrenenciesList?.size!! >0){
                val layout = LinearLayoutManager(context)
                myRecycler1?.layoutManager = layout
                val adapter = OtherCurrenenciesRecycler(this,
                    OtherCurrenenciesList?.size!!, OtherCurrenenciesList!!
                )
                myRecycler1?.adapter = adapter
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
    var progressBar: ProgressBar?=null
    var myRecycler1:RecyclerView?=null
    override fun onClickListener(clickedItemIndex: Int) {

    }

    private fun fillRecyclerList(){
        for (item in popularCountries){
            if (item!=viewModel.Currency2SelectedKey.value){
                  val value=  brain.calculateCurrentValue(1.0,viewModel.myHashMap.value?.get(item)!!, viewModel.Currency2SelectedValue.value!!
                    )
                val textValue = brain.roundToDecimalPlaces(value!!, 5)
                val conversations = Conversations(viewModel.Currency2SelectedKey.value!!,item,textValue)
                OtherCurrenenciesList?.add(conversations)
            }
        }
    }

    private fun setupChart(){
        list_for_chart?.let {
            val chartEntryModel = entryModelOf(list_for_chart!![0].toFloat(), list_for_chart!![1].toFloat(), list_for_chart!![2].toFloat())
            val chart =  myView.findViewById<ChartView>(R.id.any_chart_view)
            chart.setModel(chartEntryModel)
        }

    }

}