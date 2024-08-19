package com.example.recipemate.ui.recipe.user

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.source.local.User
import kotlinx.coroutines.launch

class UserViewModel(val repo: AuthRepository) : ViewModel() {
    private val _currentUser: MutableLiveData<User> = MutableLiveData<User>()
    val currentUser: LiveData<User> = _currentUser

    init {
        getUser()
    }

    fun getUser() = viewModelScope.launch {
        _currentUser.value = repo.findCurrentUser()
    }

    fun signOutCurrentUser() = viewModelScope.launch {
        _currentUser.value?.let {
            it.isLoggedIn = !it.isLoggedIn
            repo.updateCurrentUser(it)
        }
    }

    fun updateUserImage(imageUri: Uri) = viewModelScope.launch {
        _currentUser.value?.imageUri = imageUri
        _currentUser.value?.let { repo.updateCurrentUser(it) }
    }

    fun updateUserPass(password: String) = viewModelScope.launch {
        _currentUser.value?.password = password
        _currentUser.value?.let { repo.updateCurrentUser(it) }
    }
}


