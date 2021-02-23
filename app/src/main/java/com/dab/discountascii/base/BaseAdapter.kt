package com.dab.discountascii.base

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<E : Any?, VH : RecyclerView.ViewHolder>(
    recyclerView: RecyclerView,
    context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var data: MutableList<E?> = mutableListOf()
    protected var mRecyclerView: RecyclerView = recyclerView
    protected var mContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createMainViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    protected abstract fun createMainViewHolder(parent: ViewGroup): VH

    open fun refreshItems(items: List<E>?) {
        if (items == null || items.isEmpty()) {
            return
        }

        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(item: E, position: Int) {
        mRecyclerView.post {
            data.add(position, item)
            notifyItemInserted(position)
        }
    }

    fun getItem(position: Int): E? {
        return if (position >= data.size) null else data[position]
    }

}