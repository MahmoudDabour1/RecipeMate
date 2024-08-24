package com.example.recipemate.data.repository

import com.example.recipemate.data.source.local.RecipeDao
import com.example.recipemate.data.source.remote.model.Category
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.data.source.remote.model.RecipeDetails
import com.example.recipemate.data.source.remote.network.RetrofitModule

class RecipeRepository(private val recipeDao: RecipeDao) {
    private val apiService = RetrofitModule.apiService

    // Network operations
    suspend fun getSearchRecipes(query: String): List<RecipeDetails> {
        val searchRecipes = apiService.getSearchRecipes(query)
        return searchRecipes.recipes ?: emptyList()
    }

    suspend fun getRecipe(): List<Recipe> {
        val recipes = apiService.getRecipes()
        return recipes.recipe ?: emptyList()
    }

    suspend fun fetchRecipesByCategory(category: String): List<Recipe> {
        val recipe = apiService.getRecipeByCategory(category)
        return recipe.recipe ?: emptyList()
    }

    suspend fun fetchCategories(): List<Category> {
        return apiService.getAllCategories().categories ?: emptyList()
    }

    suspend fun fetchRecipeDetails(id: String): List<RecipeDetails> {
        val recipeDetails = apiService.getRecipeDetails(id)
        return recipeDetails.recipes ?: emptyList()
    }

    // Local database operations with userId
    suspend fun addRecipeToFav(recipe: Recipe, userEmail: String) {
        recipe.userEmail = userEmail
        recipeDao.addFavRecipe(recipe)
    }

    suspend fun deleteRecipeFromFav(recipe: Recipe) {
        recipeDao.deleteFavRecipe(recipe)
    }

    suspend fun getAllFavRecipes(userEmail: String): List<Recipe> {
        return recipeDao.getAllFavRecipes(userEmail)
    }

    suspend fun updateRecipes(recipe: Recipe) {
        recipeDao.updateRecipes(recipe)
    }

    suspend fun isRecipeInDatabase(recipe: Recipe, userEmail: String): Boolean {
        return recipe.idMeal?.let { recipeDao.isRecipeInDatabase(it, userEmail) } ?: false
    }
}
