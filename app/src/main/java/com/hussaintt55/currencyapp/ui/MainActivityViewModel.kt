package com.hussaintt55.currencyapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hussaintt55.currencyapp.model.CurrencyList.CurrencyListResponse
import com.hussaintt55.currencyapp.repository.Repo

class MainActivityViewModel : ViewModel() {
    private val Trigger: MutableLiveData<Boolean> = MutableLiveData()
    val currencyListResponse: LiveData<CurrencyListResponse>
    =Transformations.switchMap(Trigger){
        Repo.getCurrencyList() }


/*
    val currencyListResponse: LiveData<CurrencyListResponse>
            = Repo.getCurrencyList()*/


    fun Trigger(trigger: Boolean){
        val update = trigger
        if (Trigger.value == update) {
            return
        }
        Trigger.value = update
    }
}