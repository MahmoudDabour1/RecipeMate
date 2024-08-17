package com.example.recipemate.data.source.local

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val imageUri: Uri?
)
