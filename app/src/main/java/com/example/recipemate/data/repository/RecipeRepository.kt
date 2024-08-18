package com.example.recipemate.data.repository

import com.example.recipemate.data.source.remote.model.RecipeDetails
import com.example.recipemate.data.source.remote.model.Category
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.data.source.remote.network.RetrofitModule

class RecipeRepository {
    private val apiService = RetrofitModule.apiService


    suspend fun getSearchRecipes(query: String): ArrayList<RecipeDetails> {
        val searchRecipes = apiService.getSearchRecipes(query)
        return searchRecipes.recipes as ArrayList<RecipeDetails>
    }

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

    suspend fun fetchRecipeDetails(id: String): ArrayList<RecipeDetails> {
        val recipeDetails = apiService.getRecipeDetails(id)
        return recipeDetails.recipes as ArrayList<RecipeDetails>
    }
}