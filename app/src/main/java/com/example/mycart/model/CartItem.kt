package com.example.mycart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    var userId: String = "",
    var productOwnerId : String = "",
    var productId: String = "",
    var productName: String = "",
    var price: String = "",
    var image: String = "",
    var cartQuantity: String = "",
    var stockQuantity: String = "",
    var id: String = "",

    ) : Parcelable