package com.example.recipemate.data.source.remote.network

import com.example.recipeapplication.data.source.remote.network.RetrofitModule.SEARCH_END_POINT
import com.example.recipemate.data.source.SearchRecipes
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(SEARCH_END_POINT)
    suspend fun getSearchRecipes(@Query("s") query: String): SearchRecipes

}