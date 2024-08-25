package com.example.recipemate.data.source.local

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    var password: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    var imageUri: Uri?,
    val isMale: Boolean,
    var isLoggedIn: Boolean = false,


    )
