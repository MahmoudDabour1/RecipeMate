package com.example.recipemate.data.repository

import com.example.recipemate.data.source.local.User
import com.example.recipemate.data.source.local.UserDao

class AuthRepository(private val userDao: UserDao) {

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun authenticateUser(username: String, password: String): Boolean {
        return userDao.getUser(username, password) != null
    }
}