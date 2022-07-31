package com.example.mycart.model.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import com.example.mycart.model.*
import com.example.mycart.utils.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await

class FireStoreRepository(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) {

    /**
     * fun for registering user details
     */
    fun registerUser(user: User): Task<Void> {
        return firebaseFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(user, SetOptions.merge())
    }

    /**
     * fun for updater user profile
     */
    fun updateUserProfile(userHashMap: HashMap<String, Any>): Task<Void> {
        return firebaseFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
    }


    /**
     * fun for upload image
     */
    fun uploadImageToCloud(
        context: Context,
        imageUri: Uri,
    ): StorageTask<UploadTask.TaskSnapshot> {
        val imageExtension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(context.contentResolver.getType(imageUri))

        val storageRef = firebaseStorage.reference.child(
            Constants.IMAGE_REFERENCE
                    + System.currentTimeMillis() + "." + imageExtension
        )

        return storageRef.putFile(imageUri)
    }

    /**
     * fun for current user details
     */
    fun getCurrentUserDetails(): Task<DocumentSnapshot> {

        return firebaseFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
    }

    /**
     * fun for get current user id
     */
    fun getCurrentUserId(): String {

        // getting current user from FirebaseAuth
        val currentUser = firebaseAuth.currentUser

        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }


    /**
     * fun for add product
     */
    fun addProduct(product: Product): Task<Void> {
        return firebaseFirestore.collection(Constants.PRODUCTS)
            .document()
            .set(product, SetOptions.merge())
    }

    /**
     * for deleting the product from database
     */
    fun deleteProduct(productId: String): Task<Void> {
        return firebaseFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
    }


    /**
     * fun for getting all product list of current user
     */
    fun getProductList(): Task<QuerySnapshot> {

        return firebaseFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId())
            .get()

    }

    /**
     * getting list from firebase coroutine
     */
    suspend fun getProductsList(): MutableList<Product> {
        val list = ArrayList<Product>()
        return try {

            return firebaseFirestore.collection(Constants.PRODUCTS)
                .whereEqualTo(Constants.USER_ID, getCurrentUserId())
                .get().await().toObjects(Product::class.java)

        } catch (e: Exception) {
            list
        }

    }

    /**
     * for getting all product list of all users
     */
    fun getAllProducts(): Task<QuerySnapshot> {
        return firebaseFirestore.collection(Constants.PRODUCTS)
            .get()
    }

    /**
     * for add product to cart
     */
    fun addProductToCart(cartItem: CartItem): Task<Void> {
        val id = firebaseFirestore.collection(Constants.CART_ITEMS)
            .document().id

        cartItem.id = id
        return firebaseFirestore.collection(Constants.CART_ITEMS)
            .document(id)
            .set(cartItem, SetOptions.merge())
    }

    /**
     * fun for getting user cartList
     */
    fun cartList(): Task<QuerySnapshot> {
        return firebaseFirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId())
            .get()
    }

    /**
     * for knowing product exists in users cart or not
     */
    fun productExistsInUserCart(productId: String): Task<QuerySnapshot> {
        return firebaseFirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId())
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
    }

    /**
     * fun for removing product from cart
     */
    fun removeProductFromCart(cartId: String): Task<Void> {
        return firebaseFirestore.collection(Constants.CART_ITEMS)
            .document(cartId)
            .delete()
    }

    /**
     * update quantity of product in cart
     */
    fun updateProductInCart(cartId: String, hashMap: HashMap<String, Any>): Task<Void> {
        return firebaseFirestore.collection(Constants.CART_ITEMS)
            .document(cartId)
            .update(hashMap)
    }


    /**
     * for adding the address in address list
     */
    fun addAddress(address: Address): Task<Void> {
        val addressId = firebaseFirestore.collection(Constants.ADDRESS)
            .document().id

        address.addressId = addressId
        return firebaseFirestore.collection(Constants.ADDRESS)
            .document(addressId)
            .set(address, SetOptions.merge())
    }

    /**
     * for updating the address in address list
     */
    fun updateAddress(addressId: String, hashMap: HashMap<String, Any>): Task<Void> {
        return firebaseFirestore.collection(Constants.ADDRESS)
            .document(addressId)
            .update(hashMap)

    }

    /**
     * for deleting the address in address list
     */
    fun deleteAddress(addressId: String): Task<Void> {
        return firebaseFirestore.collection(Constants.ADDRESS)
            .document(addressId)
            .delete()
    }

    /**
     * for getting the address in address list
     */
    fun getAddressList(): Task<QuerySnapshot> {
        return firebaseFirestore.collection(Constants.ADDRESS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId())
            .get()
    }

    /**
     * for adding the address in address list
     */
    fun placeOrder(order: Order): Task<Void> {
        val orderId = firebaseFirestore.collection(Constants.ORDERS)
            .document().id

        order.orderId = orderId
        return firebaseFirestore.collection(Constants.ORDERS)
            .document(orderId)
            .set(order, SetOptions.merge())
    }


    /**
     * for getting orders list
     */
    fun ordersList(): Task<QuerySnapshot> {
        return firebaseFirestore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId())
            .get()
    }

    /*
    suspend fun ordersList2(): LiveData<List<Order>> {

        val list = ArrayList<Order>()
        return try {

            return firebaseFirestore.collection(Constants.ORDERS)
                .whereEqualTo(Constants.USER_ID, getCurrentUserId())
                .get().await().toObjects(Order::class.java)

        } catch (e: Exception) {
            list as LiveData<List<Order>>
        }

    }
     */


    /**
     * for updating all data after placing the order
     */
    fun updateAllDataAfterPlaceOrder(cartList: List<CartItem>, order: Order): Task<Void> {
        // fireStore writeBatch use for multiple operation at single time
        val writeBatch = firebaseFirestore.batch()

        for (cartItem in cartList) {

            val soldProduct = SoldProduct()
            soldProduct.userId = cartItem.productOwnerId
            soldProduct.title = cartItem.productName
            soldProduct.price = cartItem.price
            soldProduct.soldQuantity = cartItem.cartQuantity
            soldProduct.image = cartItem.image
            soldProduct.orderId = order.orderId
            soldProduct.orderDate = order.dateTime
            soldProduct.subTotal = order.subTotal
            soldProduct.shippingCharge = order.shippingCharge
            soldProduct.totalAmount = order.totalAmount
            soldProduct.address = order.address

            val id = firebaseFirestore.collection(Constants.SOLD_PRODUCTS)
                .document().id

            soldProduct.soldProductId = id

            val docRef = firebaseFirestore.collection(Constants.SOLD_PRODUCTS)
                .document(id)

            writeBatch.set(docRef, soldProduct)
        }

        for (cartItem in cartList) {
            val productHashMap = HashMap<String, Any>()

            productHashMap[Constants.QUANTITY] =
                (cartItem.stockQuantity.toInt() - cartItem.cartQuantity.toInt()).toString()

            val docRef = firebaseFirestore.collection(Constants.PRODUCTS)
                .document(cartItem.productId)

            writeBatch.update(docRef, productHashMap)
        }

        for (cartItem in cartList) {
            val docRef = firebaseFirestore.collection(Constants.CART_ITEMS)
                .document(cartItem.id)

            writeBatch.delete(docRef)
        }
        return writeBatch.commit()
    }

    fun getSoldProducts(): Task<QuerySnapshot> {

        return firebaseFirestore.collection(Constants.SOLD_PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserId())
            .get()
    }


}