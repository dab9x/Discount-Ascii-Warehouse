package com.dab.discountascii.view.activity

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dab.discountascii.R
import com.dab.discountascii.base.BaseActivity
import com.dab.discountascii.base.BaseViewModel
import com.dab.discountascii.databinding.ActivityMainBinding
import com.dab.discountascii.extension.hideSoftKeyboard
import com.dab.discountascii.extension.launchActivity
import com.dab.discountascii.extension.toast
import com.dab.discountascii.model.User
import com.dab.discountascii.view.adapter.UserAdapter
import com.dab.discountascii.viewmodel.MainViewModel

class MainActivity : BaseActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var userAdapter: UserAdapter? = null
    private var userFilter: MutableList<User> = mutableListOf()

    override fun setupView(savedInstanceState: Bundle?) {
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mainBinding.rcvData.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun setEvent() {
        mainBinding.refreshData.setOnRefreshListener { loadData() }

        mainBinding.ivSearch.setOnClickListener { searchUser(mainBinding.edtSearch.text.toString()) }

        mainBinding.edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchUser(mainBinding.edtSearch.text.toString())
                return@OnEditorActionListener true
            }
            false
        })

    }

    override fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setObserveLive(viewModel)

        val userObserver = Observer<List<User>> {
            setUpUser(it)
        }
        viewModel.showUsers().observe(this, userObserver)
    }

    override fun setObserveLive(viewModel: BaseViewModel) {
        viewModel.eventLoading.observe(this, {
            if (it != null) {
                if (it.getContentIfNotHandled() != null) {
                    if (it.peekContent()) {
                        showLoadingDialog()
                    } else {
                        hideLoadingDialog()
                    }
                }
            }
        })
        viewModel.eventFailure.observe(this, {
            if (it != null) {
                if (it.getContentIfNotHandled() != null) {
                    showFailure(it.peekContent())
                }
            }
        })
    }

    override fun loadData() {
        viewModel.getUsers()
    }

    private fun showLoadingDialog() {
        mainBinding.pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoadingDialog() {
        mainBinding.pbLoading.visibility = View.GONE
    }

    private fun showFailure(throwable: Throwable) {
        if (throwable.message != null) {
            toast(throwable.message.toString())
            mainBinding.tvNoData.visibility = View.VISIBLE
        }
    }

    private fun setUpUser(users: List<User>) {
        if (userFilter.isEmpty()) {
            userFilter = users.toMutableList()
        }
        mainBinding.refreshData.isRefreshing = false

        userAdapter = UserAdapter(mainBinding.rcvData, this)

        userAdapter?.onRecyclerItemInteractor = object :
            UserAdapter.OnRecyclerItemInteractor<User> {

            override fun onItemClick(v: View, item: User, position: Int) {
                launchActivity<UserPurchaseActivity> {
                    putExtra("user", item)
                }
            }

        }
        mainBinding.rcvData.adapter = userAdapter
        userAdapter?.refreshItems(users.toMutableList())

        if (users.isNotEmpty()) {
            mainBinding.tvNoData.visibility = View.GONE
        } else {
            mainBinding.tvNoData.visibility = View.VISIBLE
        }

        mainBinding.executePendingBindings()
    }

    private fun searchUser(name: String) {
        hideSoftKeyboard()

        if (userFilter.isNotEmpty() && name.isNotEmpty()) {
            val users = userFilter.filter {
                it.username == name
            }

            if (users.isNotEmpty()) {
                viewModel.filterUser(users.toMutableList())
            } else {
                toast("User with username of '${name}' was not found")
            }
        } else {
            viewModel.filterUser(userFilter)
        }
    }
}