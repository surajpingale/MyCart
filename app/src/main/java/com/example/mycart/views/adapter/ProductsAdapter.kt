package com.example.mycart.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycart.R
import com.example.mycart.databinding.ItemProductBinding
import com.example.mycart.model.Product
import com.example.mycart.utils.ProductDiffUtils

class ProductsAdapter(
    private val context: Context,
    private var productsList: List<Product>,
    private val onItemClick: OnItemClick
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productsList[position]

        holder.binding.apply {
            Glide.with(context)
                .load(product.image)
                .placeholder(R.drawable.ic_user_placeholder)
                .into(ivProduct)

            tvProductName.text = product.productName
            tvProductPrice.text = product.price
        }

        holder.itemView.setOnClickListener {
            onItemClick.onItemClicked(product)
        }

        holder.itemView.setOnLongClickListener {
            popUpInflater(holder.itemView, product)
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    fun updateList(newProductList: List<Product>) {
        val diffUtil = ProductDiffUtils(productsList, newProductList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        productsList = newProductList
        diffResult.dispatchUpdatesTo(this)
    }


    inner class ProductViewHolder(itemView: ItemProductBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: ItemProductBinding

        init {
            binding = itemView
        }
    }

    interface OnItemClick {
        fun onItemClicked(product: Product)
        fun onPopUpEditClicked(product: Product)
        fun onPopUpDeleteClicked(product: Product)
    }

    private fun popUpInflater(
        ivPopUp: View,
        product: Product
    ) {
        val popupMenu = PopupMenu(context, ivPopUp)
        popupMenu.menuInflater.inflate(R.menu.pop_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->

            if (menuItem.itemId == R.id.menu_edit) {
                onItemClick.onPopUpEditClicked(product)
            } else {
                onItemClick.onPopUpDeleteClicked(product)
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

}