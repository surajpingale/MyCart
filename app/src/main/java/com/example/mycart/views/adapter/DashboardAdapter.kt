package com.example.mycart.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycart.R
import com.example.mycart.databinding.ItemDashboardProductBinding
import com.example.mycart.model.Product
import com.example.mycart.utils.ProductDiffUtils

class DashboardAdapter(
    private val context: Context,
    private var productList: List<Product>,
    private val onDashboardItemClick: OnDashboardItemClick

) : RecyclerView.Adapter<DashboardAdapter.DashProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDashboardProductBinding.inflate(layoutInflater, parent, false)
        return DashProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashProductViewHolder, position: Int) {
        val product = productList[position]

        holder.binding.apply {
            Glide.with(context)
                .load(product.image)
                .placeholder(R.drawable.ic_user_placeholder)
                .into(ivProduct)

            tvProductName.text = product.productName
            tvProductPrice.text = product.price
        }

        holder.itemView.setOnClickListener {
            onDashboardItemClick.onItemClicked(product)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateList(newProductList: List<Product>) {
        val diffUtil = ProductDiffUtils(productList, newProductList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        productList = newProductList
        diffResult.dispatchUpdatesTo(this)
    }


    inner class DashProductViewHolder(itemView: ItemDashboardProductBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: ItemDashboardProductBinding

        init {
            binding = itemView
        }
    }


    interface OnDashboardItemClick {
        fun onItemClicked(product: Product)
    }

}