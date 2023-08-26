package com.hussaintt55.currencyapp.ui

import DataRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussaintt55.currencyapp.model.CurrencyList.CurrencyListResponse
import kotlinx.coroutines.launch

class MyViewModel() : ViewModel() {

    private val _data = MutableLiveData<Result<CurrencyListResponse>>()
    val data: LiveData<Result<CurrencyListResponse>> = _data

    fun fetchData() {
        viewModelScope.launch {
            val result = DataRepository.fetchData()
            _data.value = result
        }
    }
}
