package com.example.recipeapplication.data.source.remote.network

import com.example.recipeapplication.data.source.remote.network.RetrofitModule.LIST_ALL_CATEGORIES_END_POINT
import com.example.recipeapplication.data.source.remote.network.RetrofitModule.MEALS_END_POINT
import com.example.recipemate.data.source.remote.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(MEALS_END_POINT)
    suspend fun getRecipe(@Query("c") category: String = "Seafood"): RecipeResponse

    @GET(LIST_ALL_CATEGORIES_END_POINT)
    suspend fun getAllCategories(): List<String>

}