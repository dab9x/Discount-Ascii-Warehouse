package com.dab.discountascii.view.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import com.dab.discountascii.R
import com.dab.discountascii.base.BaseActivity
import com.dab.discountascii.base.BaseViewModel
import com.dab.discountascii.databinding.ActivitySplashBinding
import com.dab.discountascii.extension.launchActivity

class SplashActivity : BaseActivity() {

    private lateinit var splashBinding: ActivitySplashBinding

    override fun setupView(savedInstanceState: Bundle?) {
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    override fun setEvent() {
    }

    override fun setupViewModel() {

    }

    override fun setObserveLive(viewModel: BaseViewModel) {
    }

    override fun loadData() {
        Handler(Looper.getMainLooper()).postDelayed({
            launchActivity<MainActivity> {
                finish()
            }
        }, 1000)
    }
}