package com.dab.discountascii.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dab.discountascii.R
import com.dab.discountascii.data.entities.User
import com.dab.discountascii.databinding.UsersFragmentBinding
import com.dab.discountascii.extension.hideSoftKeyboard
import com.dab.discountascii.extension.toast
import com.dab.discountascii.ui.adapter.UserAdapter
import com.dab.discountascii.utils.Resource
import com.dab.discountascii.utils.autoCleared
import com.dab.discountascii.viewmodel.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class UserFragment : Fragment(), UserAdapter.UserItemListener {

    private var binding: UsersFragmentBinding by autoCleared()
    private val viewModel: UsersViewModel by viewModels()
    private lateinit var adapter: UserAdapter
    private var userFilter: MutableList<User> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UsersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter(this)
        binding.rcvData.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvData.adapter = adapter

        binding.refreshData.setOnRefreshListener {
           loadData()
        }

        binding.ivSearch.setOnClickListener { searchUser(binding.edtSearch.text.toString()) }

        binding.edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchUser(binding.edtSearch.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun setupObservers() {
        viewModel.getUsers().observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.refreshData.isRefreshing = false

                    if (it.data == null || it.data.isEmpty()) {
                        binding.tvNoData.visibility = View.VISIBLE
                    } else {
                        binding.tvNoData.visibility = View.GONE
                    }

                    it.data?.let { it1 ->
                        if (it1.isNotEmpty()) {
                            if (userFilter.isEmpty()) {
                                userFilter.addAll(ArrayList(it1))
                            }
                            adapter.setItems(ArrayList(it1))
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.refreshData.isRefreshing = false
                    it.message?.let { it1 ->
                        toast(it1)
                    }
                }
                Resource.Status.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.refreshData.isRefreshing = false
                    binding.tvNoData.visibility = View.GONE
                }
            }
        })
    }

    private fun loadData() {
        viewModel.fetchUsers()
    }

    private fun searchUser(name: String) {
        activity?.hideSoftKeyboard()

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
            loadData()
        }
    }

    override fun onClickedUser(username: String) {
        findNavController().navigate(
            R.id.action_userFragment_to_userPurchaseFragment,
            bundleOf("username" to username)
        )
    }
}