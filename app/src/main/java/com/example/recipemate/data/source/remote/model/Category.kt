package com.example.recipemate.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("strCategory") var strCategory: String? = null
)

data class CategoryList(
    @SerializedName("meals") val categories: List<Category>
)
