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

    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> = _registrationStatus

    fun checkIfEmailRegistered(username: String) {
        viewModelScope.launch {
            val registered = repository.isUserRegistered(username)
            _userEmailRegistered.value = registered
        }
    }

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String
    ) {
        viewModelScope.launch {
            try {
                val user = User(email, password, firstName, lastName, phoneNumber, null)
                repository.insert(user)
                _registrationStatus.value = true
            } catch (e: Exception) {
                _registrationStatus.value = false
            }
        }
    }

}