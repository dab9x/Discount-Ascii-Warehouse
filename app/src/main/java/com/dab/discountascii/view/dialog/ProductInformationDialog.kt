package com.dab.discountascii.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.dab.discountascii.R
import com.dab.discountascii.base.BaseDialog
import com.dab.discountascii.databinding.DialogProductInfoBinding
import com.dab.discountascii.model.Product

class ProductInformationDialog(context: Context, private val mBuilder: Builder) :
    BaseDialog(context) {
    override fun getLayoutResId(): Int = R.layout.dialog_product_info

    private lateinit var binding: DialogProductInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindData()
    }

    override fun setupView(savedInstanceState: Bundle?, coverLayout: FrameLayout) {
        val view = LayoutInflater.from(context).inflate(getLayoutResId(), null, false)
        val binding: DialogProductInfoBinding? = DataBindingUtil.bind(view)
        binding.let {
            if (it != null) {
                this.binding = it
            }
        }
        coverLayout.addView(binding?.root)
    }

    private fun bindData() {
        if (mBuilder.product != null) {
            binding.product = mBuilder.product
        }
        if (mBuilder.positiveTitle?.isNotBlank() == true) {
            binding.btnClose.text = mBuilder.positiveTitle
            if (mBuilder.onPositiveClickListener != null) {
                binding.btnClose.setOnClickListener {
                    mBuilder.onPositiveClickListener!!.onClick(
                        this@ProductInformationDialog,
                        binding.btnClose.id
                    )
                }
            }
        } else {
            binding.btnClose.visibility = View.GONE
        }
    }

    class Builder(private val mContext: Context) {
        private var mProduct: Product? = null
        var product: Product? = null
            get() = mProduct
            private set

        internal var positiveTitle: String? = null
            private set
        private var mCancelable: Boolean = false

        internal var onPositiveClickListener: DialogInterface.OnClickListener? = null
            private set

        fun isCancelable(): Boolean {
            return mCancelable
        }

        fun setProduct(product: Product): Builder = apply {
            mProduct = product
        }

        fun setPositiveButton(
            title: String,
            onPositiveClickListener: DialogInterface.OnClickListener
        ): Builder = apply {
            this.positiveTitle = title
            this.onPositiveClickListener = onPositiveClickListener
        }

        fun setCancelable(cancelable: Boolean): Builder = apply {
            this.mCancelable = cancelable
        }

        fun create(): ProductInformationDialog {
            return ProductInformationDialog(mContext, this)
        }
    }
}
