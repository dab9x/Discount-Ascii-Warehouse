package com.dab.discountascii.view.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dab.discountascii.R
import com.dab.discountascii.base.BaseActivity
import com.dab.discountascii.base.BaseViewModel
import com.dab.discountascii.databinding.ActivityUserPurchaseBinding
import com.dab.discountascii.extension.log
import com.dab.discountascii.extension.toast
import com.dab.discountascii.model.Product
import com.dab.discountascii.model.User
import com.dab.discountascii.view.adapter.ProductAdapter
import com.dab.discountascii.view.adapter.ProductHorizontalAdapter
import com.dab.discountascii.view.dialog.ProductInformationDialog
import com.dab.discountascii.viewmodel.UserPurchaseViewModel

class UserPurchaseActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUserPurchaseBinding
    private lateinit var viewModel: UserPurchaseViewModel
    private var productAdapter: ProductAdapter? = null
    private var popularProductAdapter: ProductHorizontalAdapter? = null
    private lateinit var user: User

    override fun setupView(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_purchase)
        user = intent.getSerializableExtra("user") as User
        binding.tvTitle.text = user.username
        binding.rcvPopular.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        binding.rcvProduct.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
        binding.nsvData.isNestedScrollingEnabled = false
    }

    override fun setEvent() {
        binding.ivBack.setOnClickListener(this)
        binding.refreshData.setOnRefreshListener {
            viewModel.clearProducts()
            loadData()
        }
    }

    override fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(UserPurchaseViewModel::class.java)
        setObserveLive(viewModel)

        val productObserver = Observer<List<Product>> {
            setUpProduct(it)
        }
        viewModel.showProducts().observe(this, productObserver)

        val popularObserver = Observer<List<Product>> {
            setUpPopularProduct(it)
        }
        viewModel.showProductPopular().observe(this, popularObserver)
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
        user.username.let {
            viewModel.getProducts(it)
            viewModel.getPopularProduct(it)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.ivBack -> {
                finish()
            }
        }
    }

    private fun showLoadingDialog() {
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoadingDialog() {
        binding.pbLoading.visibility = View.GONE
        binding.refreshData.isRefreshing = false

        productAdapter?.itemCount?.let {
            if (it <= 0) {
                binding.tvNoDataProduct.visibility = View.VISIBLE
            } else {
                binding.tvNoDataProduct.visibility = View.GONE
            }
        }

        popularProductAdapter?.itemCount?.let {
            if (it <= 0) {
                binding.tvNoData.visibility = View.VISIBLE
            } else {
                binding.tvNoData.visibility = View.GONE
            }
        }
    }

    private fun showFailure(throwable: Throwable) {
        if (throwable.message != null) {
            toast(throwable.message.toString())
        }
    }

    private fun setUpProduct(product: List<Product>) {
        productAdapter = ProductAdapter(binding.rcvProduct, this)

        productAdapter?.onRecyclerItemInteractor = object :
            ProductAdapter.OnRecyclerItemInteractor<Product> {

            override fun onItemClick(v: View, item: Product, position: Int) {
                showProductInformation(item)
            }

        }
        binding.rcvProduct.adapter = productAdapter
        productAdapter?.refreshItems(product.toMutableList())
        binding.executePendingBindings()
    }

    private fun setUpPopularProduct(product: List<Product>) {
        popularProductAdapter = ProductHorizontalAdapter(binding.rcvPopular, this)

        popularProductAdapter?.onRecyclerItemInteractor = object :
            ProductHorizontalAdapter.OnRecyclerItemInteractor<Product> {

            override fun onItemClick(v: View, item: Product, position: Int) {
                showProductInformation(item)
            }

        }
        binding.rcvPopular.adapter = popularProductAdapter
        popularProductAdapter?.refreshItems(product.toMutableList())
        binding.executePendingBindings()
    }

    private fun showProductInformation(product: Product) {
        ProductInformationDialog.Builder(this)
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