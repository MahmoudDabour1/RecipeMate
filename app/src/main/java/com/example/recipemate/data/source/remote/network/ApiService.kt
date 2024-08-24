package com.example.recipemate.data.source.remote.network

import com.example.recipemate.data.source.remote.network.RetrofitModule.SEARCH_END_POINT
import com.example.recipemate.data.source.remote.model.SearchRecipes
import retrofit2.http.GET
import retrofit2.http.Query

import com.example.recipemate.data.source.remote.model.CategoryList
import com.example.recipemate.data.source.remote.model.RecipeDetails
import com.example.recipemate.data.source.remote.model.RecipeResponse
import com.example.recipemate.data.source.remote.network.RetrofitModule.LIST_ALL_CATEGORIES_END_POINT
import com.example.recipemate.data.source.remote.network.RetrofitModule.MEALS_END_POINT
import com.example.recipemate.data.source.remote.network.RetrofitModule.RECIPE_DETAILS_END_POINT

interface ApiService {

    @GET(SEARCH_END_POINT)
    suspend fun getSearchRecipes(@Query("s") query: String): SearchRecipes

    @GET("filter.php?a=Seafood")
    suspend fun getRecipes(): RecipeResponse


    @GET(MEALS_END_POINT)
    suspend fun getRecipeByCategory(@Query("c") query: String): RecipeResponse

    @GET(LIST_ALL_CATEGORIES_END_POINT)
    suspend fun getAllCategories(): CategoryList

    @GET(RECIPE_DETAILS_END_POINT)
    suspend fun getRecipeDetails(@Query("i") query: String): SearchRecipes

}