package com.example.mycart.utils

import android.Manifest
import android.webkit.MimeTypeMap

object Constants {
    // All fragments
    const val FRAGMENT_LOGIN = "Login Fragment"
    const val FRAGMENT_REGISTER = "Register Fragment"
    const val FRAGMENT_FORGOT_PASSWORD = "Forgot Password Fragment"
    const val FRAGMENT_DASHBOARD = "Dashboard Fragment"
    const val FRAGMENT_PRODUCTS = "Products Fragment"
    const val FRAGMENT_ADD_PRODUCT = "Add Product Fragment"
    const val FRAGMENT_ORDERS = "Orders Fragment"
    const val FRAGMENT_SETTINGS = "Settings Fragment"
    const val FRAGMENT_PRODUCT_DETAILS = "Product Details Fragment"
    const val FRAGMENT_CART = "Cart Fragment"
    const val FRAGMENT_ADDRESS = "Address Fragment"
    const val FRAGMENT_ADD_ADDRESS = "Add Address Fragment"
    const val FRAGMENT_CHECKOUT = "Checkout Fragment"
    const val FRAGMENT_ORDER_DETAILS = "Order Details Fragment"
    const val FRAGMENT_PROFILE = "Profile Fragment"
    const val FRAGMENT_SOLD_PRODUCTS = "Sold Products Fragment"
    const val FRAGMENT_SOLD_PRODUCTS_DETAILS = "Sold Product Details Fragment"


    // User details
    const val USERS = "users"
    const val USER_ID = "userId"
    const val SHARED_PREFERENCES_DATABASE = "SharedPreferences"
    const val USER_NAME = "UserName"

    const val MALE = "Male"
    const val FEMALE = "Female"

    const val MOBILE = "mobileNumber"
    const val GENDER = "gender"

    const val IMAGE_REFERENCE = "Image-"
    const val IMAGE = "image"

    val permissionsArray = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    const val PERMISSION_REQUEST_CODE = 1


    // product
    const val PRODUCTS = "products"
    const val PRODUCT_ID = "productId"
    const val QUANTITY = "quantity"

    // cart
    const val DEFAULT_CART_QUANTITY = "1"
    const val CART_ITEMS = "cart_items"
    const val CART_QUANTITY = "cartQuantity"

    // address
    const val ADDRESS = "address"
    const val ADDRESS_ID = "addressId"
    const val FULL_NAME = "fullName"
    const val MOBILE_NUMBER = "mobileNumber"
    const val ZIP_CODE = "zipCode"

    // Order
    const val ORDERS = "orders"
    const val STOCK_QUANTITY = "stockQuantity"

    // sold products
    const val SOLD_PRODUCTS = "sold_products"

}