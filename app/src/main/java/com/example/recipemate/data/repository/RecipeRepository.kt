package com.example.recipemate.data.repository

import com.example.recipeapplication.data.source.remote.network.RetrofitModule
import com.example.recipemate.data.source.RecipeDetails

class RecipeRepository {
    private val apiService = RetrofitModule.apiService


    suspend fun getSearchRecipes(query: String): ArrayList<RecipeDetails> {
        val searchRecipes = apiService.getSearchRecipes(query)
        return searchRecipes.recipes as ArrayList<RecipeDetails>
    }

}