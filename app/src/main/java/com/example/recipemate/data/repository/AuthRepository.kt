package com.example.recipemate.data.repository

import com.example.recipemate.data.source.local.User
import com.example.recipemate.data.source.local.UserDao

class AuthRepository(private val userDao: UserDao) {

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun authenticateUser(email: String, password: String): User? {
        return userDao.getUser(email, password)
    }

    suspend fun isEmailRegistered(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }
    suspend fun findCurrentUser() = userDao.findCurrentUser()
    suspend fun updateCurrentUser(user:User) = userDao.updateWhoIsLoggedNow(user)

}