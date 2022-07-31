package com.example.mycart.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    var id:String = "",
    var firstName:String = "",
    var lastName : String = "",
    var email : String = "",
    var image : String = "",
    var mobileNumber: Long = 0,
    var gender : String = "",
    var profileCompleted : Int = 0
) : Parcelable