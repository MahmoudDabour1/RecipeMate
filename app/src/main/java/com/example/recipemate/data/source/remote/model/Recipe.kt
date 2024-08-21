package com.example.recipemate.data.source.remote.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recipe_tb")
data class Recipe(
    @SerializedName("strMeal"      ) var strMeal      : String? = null,
    @SerializedName("strMealThumb" ) var strMealThumb : String? = null,
    @SerializedName("idMeal"       ) var idMeal       : String? = null,
    var isSelected: Boolean = false,
    var isBookmarked: Boolean = false

)

data class  RecipeResponse(
  @SerializedName("meals")  val recipe: List<Recipe>
)
