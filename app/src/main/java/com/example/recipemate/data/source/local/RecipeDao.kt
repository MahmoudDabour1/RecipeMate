package com.example.recipemate.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.recipemate.data.source.remote.model.Recipe

@Dao
interface RecipeDao{
    @Insert
    suspend fun addFavRecipe(recipe : Recipe)
    @Delete
    suspend fun deleteFavRecipe(recipe: Recipe)
    @Update
    suspend fun updateRecipes()
    @Query("Select * From recipe_tb")
    suspend fun getAllFavRecipes():List<Recipe>

}