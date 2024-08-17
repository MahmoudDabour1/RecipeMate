package com.example.recipemate.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.source.local.User
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    fun register(username: String, password: String) {
        viewModelScope.launch {

        }
    }
}