package com.example.recipemate.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.recipemate.data.source.remote.model.Recipe

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavRecipe(recipe: Recipe)

    @Delete
    suspend fun deleteFavRecipe(recipe: Recipe)

    @Update
    suspend fun updateRecipes(recipe: Recipe)

    @Query("SELECT * FROM recipe_tb WHERE userEmail = :userEmail")
    suspend fun getAllFavRecipes(userEmail: String): List<Recipe>

    @Query("SELECT EXISTS (SELECT 1 FROM recipe_tb WHERE idMeal = :id AND userEmail= :userEmail)")
    suspend fun isRecipeInDatabase(id: String, userEmail: String): Boolean
}