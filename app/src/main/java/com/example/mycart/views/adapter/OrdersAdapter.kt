package com.example.mycart.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycart.R
import com.example.mycart.databinding.ItemOrderBinding
import com.example.mycart.model.Order

class OrdersAdapter(
    private val context: Context,
    private val ordersList: List<Order>,
    private val onOrderClick: OnOrderClick
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderBinding.inflate(layoutInflater, parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]

        holder.binding.apply {
            Glide.with(context)
                .load(order.image)
                .placeholder(R.drawable.ic_user_placeholder)
                .into(ivProduct)

            tvOrderName.text = order.orderName
            tvProductPrice.text = order.totalAmount
        }

        holder.itemView.setOnClickListener {
            onOrderClick.onOrderClicked(order)
        }

    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    inner class OrderViewHolder(itemView: ItemOrderBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: ItemOrderBinding

        init {
            binding = itemView
        }
    }

    interface OnOrderClick {
        fun onOrderClicked(order: Order)
    }
}