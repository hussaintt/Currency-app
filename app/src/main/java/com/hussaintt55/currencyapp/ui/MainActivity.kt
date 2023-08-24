package com.hussaintt55.currencyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.hussaintt55.currencyapp.R

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findNavController(R.id.currencyFragment).setGraph(R.navigation.main_nav)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.currencyListResponse.observe(this) {
            println("DEBUG $it")
        }

        viewModel.Trigger(true)


    }
}