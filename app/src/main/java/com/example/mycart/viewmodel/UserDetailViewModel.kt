package com.example.mycart.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mycart.model.*
import com.example.mycart.model.repository.FireStoreRepository
import com.example.mycart.utils.Response

class UserDetailViewModel(private val repository: FireStoreRepository) : ViewModel() {

    // for registering user
    private var registerUserMutable = MutableLiveData<Response<String>>()
    val registerUserLiveData: LiveData<Response<String>>
        get() = registerUserMutable

    // for creating user
    private var createUserMutable = MutableLiveData<Response<String>>()
    val createUserLiveData: LiveData<Response<String>>
        get() = createUserMutable

    // for sign in or login user
    private var loginUserMutable = MutableLiveData<Response<String>>()
    val loginUserLiveData: LiveData<Response<String>>
        get() = loginUserMutable


    // for updating user details
    private var updateUserProfileMutable = MutableLiveData<Response<String>>()
    val updateUserProfileLiveData: LiveData<Response<String>>
        get() = updateUserProfileMutable

    // for storing user details
    private var userMutableData = MutableLiveData<User>()
    val userLiveData: LiveData<User>
        get() = userMutableData

    // for product list
    private var productsMutableList = MutableLiveData<Response<List<Product>>>()
    val productLiveDataList: LiveData<Response<List<Product>>>
        get() = productsMutableList

    // for cartItem
    private var cartMutableLiveData = MutableLiveData<Response<String>>()
    val cartData: LiveData<Response<String>>
        get() = cartMutableLiveData

    // for user cart list
    private var cartListMutableData = MutableLiveData<Response<List<CartItem>>>()
    val cartListData: LiveData<Response<List<CartItem>>>
        get() = cartListMutableData

    // product exist in cart or not
    private var itemExistIn = MutableLiveData<Response<Boolean>>()
    val existProductInCart: LiveData<Response<Boolean>>
        get() = itemExistIn

    // for deleting product from cart
    private var deleteFromCartMutable = MutableLiveData<Response<String>>()
    val deletedProductFromCart: LiveData<Response<String>>
        get() = deleteFromCartMutable

    // for updating quantity in cart
    private var updateCartQuantity = MutableLiveData<Response<String>>()
    val updateCartLiveData: LiveData<Response<String>>
        get() = updateCartQuantity

    // for adding address
    private var addAddressMutable = MutableLiveData<Response<String>>()
    val addAddressLiveData: LiveData<Response<String>>
        get() = addAddressMutable

    // for getting address
    private var addressListMutable = MutableLiveData<Response<List<Address>>>()
    val addressLiveData: LiveData<Response<List<Address>>>
        get() = addressListMutable

    // for address update
    private var updateAddressMutable = MutableLiveData<Response<String>>()
    val updateAddressLive: LiveData<Response<String>>
        get() = updateAddressMutable

    // for address delete
    private var deleteAddressMutable = MutableLiveData<Response<String>>()
    val deleteAddressLive: LiveData<Response<String>>
        get() = deleteAddressMutable

    // for placing order
    private var addOrderMutable = MutableLiveData<Response<String>>()
    val addOrderLive: LiveData<Response<String>>
        get() = addOrderMutable


    // for updating all data after placing order
    private var updateAllDataMutable = MutableLiveData<Response<String>>()
    val updateAllDataLive: LiveData<Response<String>>
        get() = updateAllDataMutable


    // for orders list
    private var orderListMutable = MutableLiveData<Response<List<Order>>>()
    val orderListLive: LiveData<Response<List<Order>>>
        get() = orderListMutable

    // for sold products list
    private var soldProductListMutable = MutableLiveData<Response<List<SoldProduct>>>()
    val soldProductListLive: LiveData<Response<List<SoldProduct>>>
        get() = soldProductListMutable


    var alreadyLoaded = false

    fun createUser(email: String, password: String) {
        val result = repository.createUserWithEmailAndPass(email, password)
        result.addOnCompleteListener { result ->

            if (result.isSuccessful) {
                createUserMutable.postValue(Response.Success("Success"))
            } else {
                createUserMutable.postValue(Response.Error("Error"))
            }

        }
    }

    fun registerUser(user: User) {
        val result = repository.registerUser(user)
        result.addOnSuccessListener {
            registerUserMutable.postValue(Response.Success("Success"))
        }
            .addOnFailureListener { exception ->
                registerUserMutable.postValue(Response.Error(exception.toString()))
            }
    }

    fun loginUser(email: String, password: String) {
        val result = repository.signInWithEmailAndPass(email, password)
        result.addOnCompleteListener { result ->

            if (result.isSuccessful) {
                loginUserMutable.postValue(Response.Success("Success"))
            } else {
                loginUserMutable.postValue(Response.Error("Error"))
            }
        }
    }


    fun getUserDetails() {
        repository.getCurrentUserDetails()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)!!
                userMutableData.value = user
                alreadyLoaded = true
            }
            .addOnFailureListener { exception ->

            }
    }

    fun getUserId(): String {
        return repository.getCurrentUserId()
    }

    fun getProductList() {

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

    fun addToCart(cartItem: CartItem) {
        val result = repository.addProductToCart(cartItem)
        result.addOnSuccessListener {
            cartMutableLiveData.postValue(Response.Success("Success"))
        }
            .addOnFailureListener { exception ->
                cartMutableLiveData.postValue(Response.Error(exception.toString()))
            }
    }

    fun gettingCartList() {
        val result = repository.cartList()
        result.addOnSuccessListener { document ->
            val cartList: ArrayList<CartItem> = ArrayList()

            for (i in document) {
                val cartItem = i.toObject(CartItem::class.java)
                cartList.add(cartItem)
            }
            cartListMutableData.postValue(Response.Success(cartList))
        }
            .addOnFailureListener { exception ->
                cartListMutableData.postValue(Response.Error(exception.toString()))
            }
    }

    fun checkProductInCartOrNot(productId: String) {
        val result = repository.productExistsInUserCart(productId)
        result.addOnSuccessListener { document ->
            if (document.documents.size > 0) {
                itemExistIn.postValue(Response.Success(true))
            } else {
                itemExistIn.postValue(Response.Success(false))
            }
        }
            .addOnFailureListener { exception ->
                itemExistIn.postValue(Response.Error(exception.toString()))
            }
    }

    fun deleteItemFromCart(cartId: String) {
        val result = repository.removeProductFromCart(cartId)

        result.addOnSuccessListener {
            deleteFromCartMutable.postValue(Response.Success("Success"))
        }
            .addOnFailureListener { exception ->
                deleteFromCartMutable.postValue(Response.Error(exception.toString()))
            }
    }

    fun updateCartQuantity(cartId: String, hashMap: HashMap<String, Any>) {
        val result = repository.updateProductInCart(cartId, hashMap)

        result.addOnSuccessListener {
            updateCartQuantity.postValue(Response.Success("Success"))
        }
            .addOnFailureListener { exception ->
                updateCartQuantity.postValue(Response.Error(exception.toString()))
            }
    }

    fun addAddress(address: Address) {
        val result = repository.addAddress(address)

        result.addOnSuccessListener {
            addAddressMutable.postValue(Response.Success("Success"))
        }
            .addOnFailureListener { exception ->
                addAddressMutable.postValue(Response.Error(exception.toString()))
            }

    }

    fun getAddressList() {
        val result = repository.getAddressList()

        result.addOnSuccessListener { document ->
            val addressList = ArrayList<Address>()
            for (i in document) {
                val address = i.toObject(Address::class.java)
                addressList.add(address)
            }
            addressListMutable.postValue(Response.Success(addressList))
        }
            .addOnFailureListener { exception ->
                addressListMutable.postValue(Response.Error(exception.toString()))
            }
    }

    fun deleteAddress(addressId: String) {
        val result = repository.deleteAddress(addressId)
        result.addOnSuccessListener {
            deleteAddressMutable.postValue(Response.Success("Success"))
        }
            .addOnFailureListener { exception ->
                deleteAddressMutable.postValue(Response.Error(exception.toString()))
            }
    }

    fun updateAddress(addressId: String, hashMap: HashMap<String, Any>) {
        val result = repository.updateAddress(addressId, hashMap)
        result.addOnSuccessListener {
            updateAddressMutable.postValue(Response.Success("Success"))
        }
            .addOnFailureListener { exception ->
                updateAddressMutable.postValue(Response.Error(exception.toString()))
            }
    }

    fun placeOrder(order: Order) {
        val result = repository.placeOrder(order)
        result.addOnSuccessListener {
            addOrderMutable.postValue(Response.Success("Success"))
        }
            .addOnFailureListener { exception ->
                addOrderMutable.postValue(Response.Error(exception.toString()))
            }
    }

    fun updateAllDataAfterPlaceOrder(cartList: List<CartItem>, order: Order) {
        val result = repository.updateAllDataAfterPlaceOrder(cartList, order)

        result.addOnSuccessListener {
            updateAllDataMutable.postValue(Response.Success("Success"))
        }
            .addOnFailureListener { exception ->
                updateAllDataMutable.postValue(Response.Error(exception.toString()))
            }

    }

    fun getOrdersList() {
        val result = repository.ordersList()
        result.addOnSuccessListener { document ->

            val ordersList = ArrayList<Order>()

            for (i in document) {
                val order = i.toObject(Order::class.java)
                ordersList.add(order)
            }
            orderListMutable.postValue(Response.Success(ordersList))
        }
            .addOnFailureListener { exception ->
                orderListMutable.postValue(Response.Error(exception.toString()))
            }
    }


    fun getSoldProducts() {
        val result = repository.getSoldProducts()
        result.addOnSuccessListener { document ->

            val list = ArrayList<SoldProduct>()
            for (i in document) {
                val soldProduct = i.toObject(SoldProduct::class.java)
                soldProduct.soldProductId = i.id
                list.add(soldProduct)
            }

            soldProductListMutable.postValue(Response.Success(list))

        }
            .addOnFailureListener { exception ->
                soldProductListMutable.postValue(Response.Error(exception.toString()))
            }
    }

    fun updateUserProfile(hashMap: HashMap<String, Any>) {
        val result = repository.updateUserProfile(hashMap)
        result.addOnSuccessListener {
            updateUserProfileMutable.postValue(Response.Success("Success"))
        }
            .addOnFailureListener { exception ->
                updateUserProfileMutable.postValue(Response.Error(exception.toString()))
            }
    }

}