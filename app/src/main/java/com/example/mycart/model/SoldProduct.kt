package com.example.mycart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SoldProduct(

    var soldProductId: String = "",
    var userId: String = "",
    var title: String = "",
    var price: String = "",
    var soldQuantity: String = "",
    var image: String = "",
    var orderId: String = "",
    var orderDate: Long = 0L,
    var subTotal: String = "",
    var shippingCharge: String = "",
    var totalAmount: String = "",
    var address: Address = Address()

) : Parcelable
