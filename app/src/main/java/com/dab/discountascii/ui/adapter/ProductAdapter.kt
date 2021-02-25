package com.dab.discountascii.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dab.discountascii.data.entities.Product
import com.dab.discountascii.databinding.ItemProductBinding

class ProductAdapter(private val listener: ProductItemListener) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    interface ProductItemListener {
        fun onClickedProduct(product: Product)
    }

    private val items = ArrayList<Product>()

    fun setItems(items: ArrayList<Product>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(item: Product) {
        this.items.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding: ItemProductBinding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        if (items.size > 0) {
            holder.bind(items[position])
        }
    }

    inner class ProductViewHolder(
        private val itemBinding: ItemProductBinding,
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

