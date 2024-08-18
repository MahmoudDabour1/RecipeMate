package com.example.recipemate.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.source.local.User
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<User?>()
    val loginResult: LiveData<User?> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val currentUser = repository.authenticateUser(email, password)
            if(currentUser!=null) {
                currentUser.isLoggedIn=!currentUser.isLoggedIn
                repository.updateCurrentUser(currentUser)
            }
            _loginResult.value = currentUser
        }
    }
}