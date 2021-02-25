package com.dab.discountascii.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dab.discountascii.R
import com.dab.discountascii.data.entities.Product
import com.dab.discountascii.databinding.UserPurchaseFragmentBinding
import com.dab.discountascii.extension.toast
import com.dab.discountascii.ui.activity.UserActivity
import com.dab.discountascii.ui.adapter.ProductAdapter
import com.dab.discountascii.ui.adapter.ProductHorizontalAdapter
import com.dab.discountascii.ui.dialog.ProductInformationDialog
import com.dab.discountascii.utils.Resource
import com.dab.discountascii.utils.autoCleared
import com.dab.discountascii.viewmodel.UsersPurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UserPurchaseFragment : Fragment(), ProductAdapter.ProductItemListener,
    ProductHorizontalAdapter.ProductItemListener {

    private var binding: UserPurchaseFragmentBinding by autoCleared()
    private val viewModel: UsersPurchaseViewModel by viewModels()
    private lateinit var adapter: ProductAdapter
    private lateinit var adapterPopular: ProductHorizontalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserPurchaseFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("username")?.let {
            (requireActivity() as UserActivity).updateTitle(it)
            viewModel.start(it)
        }
        setupRecyclerView()
        setupObservers()
        loadData()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(this)
        binding.rcvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rcvProduct.adapter = adapter
        binding.rcvProduct.isNestedScrollingEnabled = false

        adapterPopular = ProductHorizontalAdapter(this)
        binding.rcvPopular.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rcvPopular.adapter = adapterPopular
        binding.rcvPopular.isNestedScrollingEnabled = false

        binding.refreshData.setOnRefreshListener {
            loadData()
        }

    }

    private fun setupObservers() {
        viewModel.getProducts().observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.pbLoading.visibility = View.GONE
                    binding.refreshData.isRefreshing = false
                    if (it.data == null || it.data.isEmpty()) {
                        binding.tvNoDataProduct.visibility = View.VISIBLE
                    } else {
                        binding.tvNoDataProduct.visibility = View.GONE
                    }

                    it.data?.let { it1 ->
                        if (it1.isNotEmpty()) {
                            adapter.setItems(ArrayList(it1))
                        }
                    }
                }

                Resource.Status.ERROR -> {
                    it.message?.let { it1 ->
                        toast(it1)
                    }
                    binding.pbLoading.visibility = View.GONE
                    binding.tvNoDataProduct.visibility = View.GONE
                    binding.refreshData.isRefreshing = false
                }

                Resource.Status.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.tvNoDataProduct.visibility = View.GONE
                    binding.refreshData.isRefreshing = false
                }
            }
        })

        viewModel.getProductsPopular().observe(viewLifecycleOwner, {
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
                        val listSorted = it1.sortedWith(
                            compareByDescending {i12 ->
                                i12.price
                            }
                        ).sortedWith(
                            compareByDescending {it3 ->
                                it3.recent.size
                            }
                        )
                        adapterPopular.setItems(ArrayList(listSorted))
                    }
                }

                Resource.Status.ERROR -> {
                    it.message?.let { it1 ->
                        toast(it1)
                    }
                    binding.pbLoading.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.refreshData.isRefreshing = false
                }

                Resource.Status.LOADING -> {
                    binding.pbLoading.visibility = View.VISIBLE
                    binding.tvNoData.visibility = View.GONE
                    binding.refreshData.isRefreshing = false
                }
            }
        })
    }

    private fun loadData() {
        viewModel.fetchProducts()
        viewModel.fetchProductsPopular()
    }

    override fun onClickedProduct(product: Product) {
        context?.let {
            ProductInformationDialog.Builder(it)
                .setProduct(product)
                .setPositiveButton(
                    getString(R.string.close)
                ) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .create()
                .show()
        }

    }
}