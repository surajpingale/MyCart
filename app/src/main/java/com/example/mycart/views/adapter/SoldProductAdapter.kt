package com.example.mycart.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycart.databinding.ItemOrderBinding
import com.example.mycart.model.SoldProduct

class SoldProductAdapter(
    private val context: Context,
    private val soldProductList: List<SoldProduct>,
    private val onSoldProductClick: OnSoldProductClick
) : RecyclerView.Adapter<SoldProductAdapter.SoldProductViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoldProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderBinding.inflate(layoutInflater, parent, false)
        return SoldProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SoldProductViewHolder, position: Int) {
        val soldProduct  = soldProductList[position]

        holder.binding.apply {
            Glide.with(context)
                .load(soldProduct.image)
                .into(ivProduct)
            tvOrderName.text = soldProduct.title
            tvProductPrice.text = soldProduct.totalAmount
        }

        holder.itemView.setOnClickListener {
            onSoldProductClick.onSoldProductClicked(soldProduct)
        }
    }

    override fun getItemCount(): Int {
        return soldProductList.size
    }


    inner class SoldProductViewHolder(itemView : ItemOrderBinding) :
        RecyclerView.ViewHolder(itemView.root)
    {
        var binding : ItemOrderBinding
        init {
            binding = itemView
        }
    }

    interface OnSoldProductClick {
        fun onSoldProductClicked(soldProduct: SoldProduct)
    }
}