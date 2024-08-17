package com.example.recipemate.data.repository

import com.example.recipeapplication.data.source.remote.network.RetrofitModule
import com.example.recipemate.data.source.remote.model.Category
import com.example.recipemate.data.source.remote.model.Recipe

class RecipeRepository {

    private val apiService = RetrofitModule.apiService

    suspend fun getRecipe(): List<Recipe> {
        val recipes = apiService.getRecipes()
        return recipes.recipe
    }

    suspend fun fetchRecipesByCategory(category: String): List<Recipe> {
        val recipe = apiService.getRecipeByCategory(category)
        return recipe.recipe

    }


    suspend fun fetchCategories(): List<Category> {
        return apiService.getAllCategories().categories
    }
}