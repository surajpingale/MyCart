package com.example.mycart.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycart.model.Product
import com.example.mycart.model.User
import com.example.mycart.model.repository.FireStoreRepository
import com.example.mycart.utils.Response

class AddProductViewModel(
    private val repository: FireStoreRepository
) : ViewModel() {

    // for user details data
    private var userMutableData = MutableLiveData<Response<User>>()
    val userDetailLiveData: LiveData<Response<User>>
        get() = userMutableData

    // for product uploading data
    private var productMutableData = MutableLiveData<Response<Any>>()
    val productLiveData: LiveData<Response<Any>>
        get() = productMutableData

    // for image uploading data
    private var imageMutableData = MutableLiveData<Response<String>>()
    val imageLiveData: LiveData<Response<String>>
        get() = imageMutableData


    fun addProduct(product: Product) {
        val response = repository.addProduct(product)
        response.addOnSuccessListener {
            productMutableData.postValue(Response.Success("Success"))
        }.addOnFailureListener { exception ->
            productMutableData.postValue(Response.Error(exception.toString()))
        }
    }

    fun uploadImageToCloud(context: Context, uri: Uri) {
        val response = repository.uploadImageToCloud(context, uri)
        response.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { imageUrl ->

                    imageMutableData.postValue(Response.Success(imageUrl.toString()))
                }
                .addOnFailureListener { exception ->
                    imageMutableData.postValue(Response.Error(exception.toString()))
                }
        }

    }

    fun getUserDetails() {
        val result = repository.getCurrentUserDetails()
        result.addOnSuccessListener { document ->
            val user = document.toObject(User::class.java)!!
            userMutableData.postValue(Response.Success(user))
        }.addOnFailureListener { exception ->
            userMutableData.postValue(Response.Error(exception.toString()))
        }
    }
}