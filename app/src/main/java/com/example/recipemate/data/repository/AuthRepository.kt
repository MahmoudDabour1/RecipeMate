package com.example.recipemate.data.repository

import com.example.recipemate.data.source.local.User
import com.example.recipemate.data.source.local.UserDao

class AuthRepository(private val userDao: UserDao) {

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun authenticateUser(email: String, password: String): Boolean {
        return userDao.getUser(email, password) != null
    }

    suspend fun isUserRegistered(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }
}