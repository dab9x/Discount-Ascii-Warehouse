package com.dab.discountascii.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dab.discountascii.data.entities.Product
import com.dab.discountascii.databinding.ItemProductHorizontalBinding

class ProductHorizontalAdapter(private val listener: ProductItemListener) :
    RecyclerView.Adapter<ProductHorizontalAdapter.ProductViewHolder>() {

    interface ProductItemListener {
        fun onClickedProduct(product: Product)
    }

    private val items = ArrayList<Product>()

    fun setItems(items: ArrayList<Product>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding: ItemProductHorizontalBinding =
            ItemProductHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(items[position])

    inner class ProductViewHolder(
        private val itemBinding: ItemProductHorizontalBinding,
        private val listener: ProductItemListener
    ) : RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

        private lateinit var product: Product

        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: Product) {
            this.product = item
            itemBinding.product = this.product
        }

        override fun onClick(v: View?) {
            listener.onClickedProduct(product)
        }
    }

}

