package com.dab.discountascii.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dab.discountascii.R
import com.dab.discountascii.base.BaseAdapter
import com.dab.discountascii.databinding.ItemProductBinding
import com.dab.discountascii.databinding.ItemProductHorizontalBinding
import com.dab.discountascii.model.Product

class ProductHorizontalAdapter(
    recyclerView: RecyclerView,
    context: Context
) :
    BaseAdapter<Product, ProductHorizontalAdapter.MyViewHolder>(
        recyclerView,
        context
    ) {

    var onRecyclerItemInteractor: OnRecyclerItemInteractor<Product>? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            holder.binding?.product = getItem(position)
        }
    }

    override fun createMainViewHolder(parent: ViewGroup): MyViewHolder {
        val viewHolder = MyViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.item_product_horizontal, parent, false
            )
        )

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            getItem(position)?.let { it1 ->
                onRecyclerItemInteractor?.onItemClick(
                    it,
                    it1,
                    position
                )
            }
        }

        return viewHolder
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding: ItemProductHorizontalBinding? = DataBindingUtil.bind(view)

    }

    interface OnRecyclerItemInteractor<E> {
        fun onItemClick(v: View, item: E, position: Int)
    }

}