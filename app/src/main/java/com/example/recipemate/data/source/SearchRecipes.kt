package com.example.recipemate.data.source

import com.google.gson.annotations.SerializedName

data class SearchRecipes(
    @SerializedName("meals") val recipes: ArrayList<RecipeDetails>?
)
