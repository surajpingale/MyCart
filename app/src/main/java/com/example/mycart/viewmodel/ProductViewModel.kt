package com.example.mycart.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycart.model.Product
import com.example.mycart.model.repository.FireStoreRepository
import com.example.mycart.utils.Response

class ProductViewModel(private val repository: FireStoreRepository) : ViewModel() {


    private var productsMutableList = MutableLiveData<Response<List<Product>>>()
    val productLiveData get() = productsMutableList

    private var productDeleteData = MutableLiveData<Response<String>>()
    val productDeleteLiveData get() = productDeleteData

    var alreadyLoaded = false

    fun getProductList() {

        productLiveData.postValue(Response.Loading())

        val list = repository.getProductList()

        list.addOnSuccessListener { document ->
            val productList: ArrayList<Product> = ArrayList()
            for (i in document) {
                val product = i.toObject(Product::class.java)
                product.productId = i.id
                productList.add(product)
            }
            productsMutableList.postValue(Response.Success(productList))
            alreadyLoaded = true
        }
            .addOnFailureListener { exception ->
                productsMutableList.postValue(Response.Error(exception.toString()))
            }
    }

    fun productDelete(productId: String) {
        val deleteResponse = repository.deleteProduct(productId)
        deleteResponse.addOnSuccessListener {
            productDeleteData.postValue(Response.Success("Success"))
        }
            .addOnFailureListener { exception ->
                productDeleteData.postValue(Response.Error(exception.toString()))
            }
    }

}