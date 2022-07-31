package com.example.mycart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var productId : String = "",
    var userName : String = "",
    var userId : String = "",
    var productName : String = "",
    var price : String = "",
    var description : String = "",
    var quantity : String = "",
    var image : String = ""

): Parcelable
