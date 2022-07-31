package com.example.mycart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(

    var addressId: String = "",
    var userId: String = "",
    var fullName: String = "",
    var mobileNumber: String = "",
    var address: String = "",
    var zipCode: String = ""

) : Parcelable
