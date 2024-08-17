package com.example.recipemate.data.repository

import com.example.recipeapplication.data.source.remote.network.RetrofitModule
import com.example.recipemate.data.source.remote.model.Recipe

class RecipeRepository {

    private val apiService = RetrofitModule.apiService
    suspend fun getSeafoodRecipes(): ArrayList<Recipe> {
        val recipes = apiService.getRecipe()
        return recipes.recipe as ArrayList<Recipe>

    }

    suspend fun fetchCategories(): List<String> {
        return apiService.getAllCategories()
    }
}