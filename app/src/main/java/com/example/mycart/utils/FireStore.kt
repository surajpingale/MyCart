package com.example.mycart.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.mycart.interfaces.FirebaseLoginListener
import com.example.mycart.interfaces.FirebaseRegisterListener
import com.example.mycart.interfaces.UpdateProfileListener
import com.example.mycart.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage

class FireStore {

    fun registerUser(
        firebaseRegisterListener: FirebaseRegisterListener,
        fireStore: FirebaseFirestore, user: User
    ) {

        fireStore.collection(Constants.USERS)
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                firebaseRegisterListener.onSuccessListener()
            }
            .addOnFailureListener {
                firebaseRegisterListener.onFailureListener()
            }
    }

    fun updateUserProfile(
        updateProfileListener: UpdateProfileListener,
        firebaseFirestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        userHashMap: HashMap<String, Any>
    ) {
        firebaseFirestore.collection(Constants.USERS)
            .document(getCurrentUserId(firebaseAuth))
            .update(userHashMap)
            .addOnSuccessListener {
                updateProfileListener.onUpdateSuccess()
            }
            .addOnFailureListener { exception ->
                updateProfileListener.onUpdateFailed(exception)
            }

    }

    fun uploadImageToCloud(
        context: Context,
        updateProfileListener: UpdateProfileListener,
        imageUri: Uri,
        firebaseStorage: FirebaseStorage
    ) {
        val imageExtension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(context.contentResolver.getType(imageUri))

        val storageRef = firebaseStorage.reference.child(
            Constants.IMAGE_REFERENCE
                    + System.currentTimeMillis() + "." + imageExtension
        )

        storageRef.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { imageUrl ->
                    updateProfileListener.onImageUpdateSuccess(imageUrl)
                }
                .addOnFailureListener { exception ->
                    updateProfileListener.onImageUpdateFailed(exception)
                }
        }
    }

    fun getCurrentUserId(firebaseAuth: FirebaseAuth): String {

        // getting current user from FirebaseAuth
        val currentUser = firebaseAuth.currentUser

        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    fun getCurrentUserDetails(
        firebaseLoginListener: FirebaseLoginListener,
        firebaseFirestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ) {
        firebaseFirestore.collection(Constants.USERS)
            .document(getCurrentUserId(firebaseAuth))
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)!!
                firebaseLoginListener.onSuccessListener(user)
            }
            .addOnFailureListener { exception ->
                firebaseLoginListener.onFailureListener(exception)
            }
    }
}