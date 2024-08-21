package com.example.recipemate.data.repository

import com.example.recipemate.data.source.local.RecipeDao
import com.example.recipemate.data.source.remote.model.RecipeDetails
import com.example.recipemate.data.source.remote.model.Category
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.data.source.remote.network.RetrofitModule

class RecipeRepository(val recipeDao : RecipeDao) {
    private val apiService = RetrofitModule.apiService

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
    suspend fun addRecipeToFav(recipe: Recipe) = recipeDao.addFavRecipe(recipe)
    suspend fun deleteRecipeFromFav(recipe: Recipe) = recipeDao.deleteFavRecipe(recipe)
    suspend fun getAllFavRecipes() = recipeDao.getAllFavRecipes()
    suspend fun updateRecipes() = recipeDao.updateRecipes()
}