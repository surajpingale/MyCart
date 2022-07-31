package com.example.mycart.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.mycart.model.CartItem
import com.example.mycart.model.Product

class CartDiffUtils(
    private val oldList: List<CartItem>,
    private val newList: List<CartItem> ) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id == newList[newItemPosition].id ->{
                true
            }

            oldList[oldItemPosition].cartQuantity == newList[newItemPosition].cartQuantity ->{
                true
            }

            oldList[oldItemPosition].price == newList[newItemPosition].price ->{
                true
            }
            else -> false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }
            oldList[oldItemPosition].cartQuantity != newList[newItemPosition].cartQuantity ->{
                false
            }
            oldList[oldItemPosition].price != newList[newItemPosition].price ->{
                false
            }

            else -> true
        }
    }
}