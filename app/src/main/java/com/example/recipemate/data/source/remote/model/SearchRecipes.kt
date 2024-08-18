package com.example.recipemate.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class SearchRecipes(
    @SerializedName("meals") val recipes: ArrayList<RecipeDetails>?
)
