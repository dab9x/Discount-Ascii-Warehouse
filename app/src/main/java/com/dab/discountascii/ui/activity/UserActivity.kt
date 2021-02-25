package com.dab.discountascii.ui.activity

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dab.discountascii.R
import com.dab.discountascii.base.BaseActivity
import com.dab.discountascii.databinding.ActivityUsersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : BaseActivity() {

    lateinit var binding: ActivityUsersBinding

    override fun setupView(savedInstanceState: Bundle?) {
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun loadData() {

    }

    fun updateTitle(name: String) {
        binding.toolbar.title = name
    }
}