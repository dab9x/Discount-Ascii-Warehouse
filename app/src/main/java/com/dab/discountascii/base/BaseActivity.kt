package com.dab.discountascii.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView(savedInstanceState)
        loadData()
    }

    abstract fun setupView(savedInstanceState: Bundle?)

    abstract fun loadData()
}