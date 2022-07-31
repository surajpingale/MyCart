package com.example.mycart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mycart.model.repository.FireStoreRepository

class UserViewModelFactory(private val repository: FireStoreRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserDetailViewModel(repository) as T
    }
}