package com.hussaintt55.currencyapp.ui

import DataRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussaintt55.currencyapp.model.CurrencyList.CurrencyListResponse
import com.hussaintt55.currencyapp.model.Historical.HistoricalResponse
import kotlinx.coroutines.launch

class MyViewModel() : ViewModel() {


    val Currency1SelectedKey :MutableLiveData<String> = MutableLiveData()
    val Currency2SelectedKey :MutableLiveData<String> = MutableLiveData()
    val Currency1Possition :MutableLiveData<Int> = MutableLiveData()
    val Currency2Possition :MutableLiveData<Int> = MutableLiveData()
    val Currency1SelectedValue :MutableLiveData<Double> = MutableLiveData()
    val Currency2SelectedValue :MutableLiveData<Double> = MutableLiveData()
    val desierdAmount :MutableLiveData<Double> = MutableLiveData()
    val myHashMap :MutableLiveData<HashMap<String,Double>> = MutableLiveData()


    private val _data = MutableLiveData<Result<CurrencyListResponse>>()
    val data: LiveData<Result<CurrencyListResponse>> = _data

    fun fetchData() {
        viewModelScope.launch {
            val result = DataRepository.fetchData()
            _data.value = result
        }
    }


    private val _dataHistorical = MutableLiveData<Result<HistoricalResponse>>()
    val dataHistorical: LiveData<Result<HistoricalResponse>> = _dataHistorical

    fun fetchDataHistorical(date:String) {
        viewModelScope.launch {
            val result = DataRepository.fetchDataHistorical(date)
            _dataHistorical.value = result
        }
    }
}
