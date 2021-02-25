package com.dab.discountascii.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dab.discountascii.base.BaseActivity
import com.dab.discountascii.databinding.ActivitySplashBinding
import com.dab.discountascii.extension.launchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    override fun setupView(savedInstanceState: Bundle?) {
        val binding: ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun loadData() {
        Handler(Looper.getMainLooper()).postDelayed({
            launchActivity<UserActivity> {
                finish()
            }
        }, 1000)
    }
}
