package com.example.mycart.interfaces

import com.example.mycart.model.User
import java.lang.Exception

interface FirebaseLoginListener {
    fun onSuccessListener(user: User)
    fun onFailureListener(exception: Exception)
}