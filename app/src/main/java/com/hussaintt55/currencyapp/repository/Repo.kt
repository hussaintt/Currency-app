package com.hussaintt55.currencyapp.repository

import android.content.res.Resources
import androidx.lifecycle.LiveData
import com.hussaintt55.currencyapp.R
import com.hussaintt55.currencyapp.api.MyRetrofitBuider
import com.hussaintt55.currencyapp.model.CurrencyList.CurrencyListResponse
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Repo {
    var job: CompletableJob? = null
    fun getCurrencyList(): LiveData<CurrencyListResponse> {
        job = Job()
        return object : LiveData<CurrencyListResponse>() {
            override fun onActive() {
                super.onActive()
                job?.let {
                    CoroutineScope(IO + it).launch {
                        val currencyListResponse = MyRetrofitBuider.apiService.getCurrencyList(
                           "182a31e1cf69b50bba2621ee49965329"
                        )
                        withContext(Main) {
                            value = currencyListResponse
                            it.complete()
                        }
                    }
                }
            }
        }
    }

    fun cancelJobs() {
        job?.cancel()
    }
}