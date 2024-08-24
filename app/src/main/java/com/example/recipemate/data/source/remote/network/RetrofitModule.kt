package com.example.recipemate.data.source.remote.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitModule {
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    const val SEARCH_END_POINT = "search.php"
    const val MEALS_END_POINT = "filter.php"
    const val LIST_ALL_CATEGORIES_END_POINT = "list.php?c=list"
    const val RECIPE_DETAILS_END_POINT = "lookup.php"

    private val httpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

    private val gson = GsonBuilder().serializeNulls().create()

    private val retrofitClient = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient)
        .build()

    val apiService = retrofitClient.create(ApiService::class.java)

}