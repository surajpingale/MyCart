package com.example.mycart.interfaces

import android.net.Uri
import java.lang.Exception

interface UpdateProfileListener {
    fun onUpdateSuccess()
    fun onUpdateFailed(exception: Exception)
    fun onImageUpdateSuccess(imageUrl : Uri)
    fun onImageUpdateFailed(exception: Exception)
}