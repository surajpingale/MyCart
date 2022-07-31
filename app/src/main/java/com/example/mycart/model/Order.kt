package com.example.mycart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Order(
    var orderId: String = "",
    var cartList: List<CartItem> = ArrayList(),
    var address: Address = Address(),
    var orderName: String = "",
    var userId: String = "",
    var subTotal: String = "",
    var shippingCharge: String = "",
    var totalAmount: String = "",
    var dateTime : Long = 0L,
    var image: String = ""
) : Parcelable
