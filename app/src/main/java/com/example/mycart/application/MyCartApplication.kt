package com.example.mycart.application

import android.app.Application
import com.example.mycart.model.repository.FireStoreRepository
import com.example.mycart.utils.FireStore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MyCartApplication : Application() {

    val firebaseAuth by lazy {
       Firebase.auth
    }

    val firebaseFirestore by lazy {
        Firebase.firestore
    }

    val fireStore by lazy {
        FireStore()
    }

    val firebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    val fireStoreRepository by lazy {
        FireStoreRepository(firebaseStorage, firebaseAuth, firebaseFirestore)
    }

}