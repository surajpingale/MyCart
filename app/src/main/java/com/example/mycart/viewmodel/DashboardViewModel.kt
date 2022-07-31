package com.example.mycart.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycart.model.Product
import com.example.mycart.model.repository.FireStoreRepository
import com.example.mycart.utils.Response

class DashboardViewModel(private val repository: FireStoreRepository) : ViewModel() {

    private var mutableProductList = MutableLiveData<Response<List<Product>>>()

    val productLiveData get() = mutableProductList

    fun getAllProducts() {
        val allProducts = repository.getAllProducts()
        allProducts.addOnSuccessListener { document ->

            val allProductList: ArrayList<Product> = ArrayList()

            for (i in document) {
                val product = i.toObject(Product::class.java)
                product.productId = i.id
                allProductList.add(product)
            }
            mutableProductList.postValue(Response.Success(allProductList))
        }
            .addOnFailureListener { exception ->

                mutableProductList.postValue(Response.Error(exception.toString()))

            }
    }

}