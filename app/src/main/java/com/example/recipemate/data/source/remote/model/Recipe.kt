package com.example.recipemate.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("strMeal"      ) var strMeal      : String? = null,
    @SerializedName("strMealThumb" ) var strMealThumb : String? = null,
    @SerializedName("idMeal"       ) var idMeal       : String? = null

)

data class  RecipeResponse(
  @SerializedName("meals")  val recipe: List<Recipe>
)
