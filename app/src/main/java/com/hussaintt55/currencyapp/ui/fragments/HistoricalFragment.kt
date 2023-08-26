package com.hussaintt55.currencyapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.hussaintt55.currencyapp.R
import com.hussaintt55.currencyapp.ui.MyViewModel


class HistoricalFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)

        println("xxx "+viewModel.Currency1SelectedKey.value)
        println("xxx "+viewModel.Currency2SelectedKey.value)
        return inflater.inflate(R.layout.fragment_historical, container, false)
    }
}