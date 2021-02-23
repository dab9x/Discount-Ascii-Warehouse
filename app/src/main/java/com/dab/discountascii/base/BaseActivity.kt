package com.dab.discountascii.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView(savedInstanceState)
        setupViewModel()
        loadData()
        setEvent()
    }

    abstract fun setupView(savedInstanceState: Bundle?)

    abstract fun setEvent()

    abstract fun setupViewModel()

    abstract fun setObserveLive(viewModel: BaseViewModel)

    abstract fun loadData()
}