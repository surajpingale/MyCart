package com.example.mycart.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycart.R
import com.example.mycart.databinding.ItemCartBinding
import com.example.mycart.model.CartItem
import com.example.mycart.utils.CartDiffUtils

class CartAdapter(
    private val context: Context,
    private var cartList: List<CartItem>,
    private val onCartItemClick: OnCartItemClick?
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCartBinding.inflate(layoutInflater, parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartList[position]

        holder.binding.apply {
            Glide.with(context)
                .load(cartItem.image)
                .placeholder(R.drawable.ic_cart)
                .into(ivCartProduct)
            tvCartProductName.text = cartItem.productName
            tvCartProductPrice.text = cartItem.price
            tvProductInCart.text = cartItem.cartQuantity

            if (onCartItemClick != null) {
                ivCartDelete.visibility = View.VISIBLE
                ivAddProductToCart.visibility = View.VISIBLE
                ivRemoveProductFromCart.visibility = View.VISIBLE
                ivCartDelete.setOnClickListener {
                    onCartItemClick.onCartDeleteClicked(cartItem)
                }

                ivAddProductToCart.setOnClickListener {
                    onCartItemClick.onCartAddClicked(cartItem)
                }

                ivRemoveProductFromCart.setOnClickListener {
                    onCartItemClick.onCartRemoveClicked(cartItem)
                }
            } else {
                ivCartDelete.visibility = View.GONE
                ivAddProductToCart.visibility = View.GONE
                ivRemoveProductFromCart.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    fun updateCartList(newCartList: List<CartItem>) {
        val diffUtils = CartDiffUtils(cartList, newCartList)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        cartList = newCartList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class CartViewHolder(itemView: ItemCartBinding) : RecyclerView.ViewHolder(itemView.root) {
        var binding: ItemCartBinding

        init {
            binding = itemView
        }
    }

    interface OnCartItemClick {
        fun onCartDeleteClicked(cartItem: CartItem)
        fun onCartItemClicked(cartItem: CartItem)
        fun onCartAddClicked(cartItem: CartItem)
        fun onCartRemoveClicked(cartItem: CartItem)
    }
}