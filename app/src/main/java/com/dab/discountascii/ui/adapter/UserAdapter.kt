package com.dab.discountascii.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dab.discountascii.data.entities.User
import com.dab.discountascii.databinding.ItemUserBinding

class UserAdapter(private val listener: UserItemListener) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    interface UserItemListener {
        fun onClickedUser(username: String)
    }

    private val items = ArrayList<User>()

    fun setItems(items: ArrayList<User>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding: ItemUserBinding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(items[position])

    inner class UserViewHolder(
        private val itemBinding: ItemUserBinding,
        private val listener: UserItemListener
    ) : RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

        private lateinit var user: User

        init {
            itemBinding.root.setOnClickListener(this)
        }

        fun bind(item: User) {
            this.user = item
            itemBinding.user = this.user
        }

        override fun onClick(v: View?) {
            listener.onClickedUser(user.username)
        }
    }
}

