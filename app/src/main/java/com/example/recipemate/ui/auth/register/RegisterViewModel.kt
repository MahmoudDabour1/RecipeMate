package com.example.recipemate.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.source.local.User
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _userEmailRegistered = MutableLiveData<Boolean>()
    val userEmailRegistered: LiveData<Boolean> = _userEmailRegistered

    fun checkIfEmailRegistered(email: String) {
        viewModelScope.launch {
            val registered = repository.isEmailRegistered(email)
            _userEmailRegistered.value = registered
        }
    }

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        isMale: Boolean
    ) {
        viewModelScope.launch {
            val user = User(email, password, firstName, lastName, phoneNumber, null, isMale)
            repository.insert(user)

        }
    }

}