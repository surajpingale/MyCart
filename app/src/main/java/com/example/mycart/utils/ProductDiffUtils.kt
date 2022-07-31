package com.example.mycart.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.mycart.model.Product

class ProductDiffUtils(
    private val oldList: List<Product>,
    private val newList: List<Product> ) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].productId == newList[newItemPosition].productId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].productId != newList[newItemPosition].productId -> {
                false
            }
            else -> true
        }
    }
}