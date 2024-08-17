package com.example.recipeapplication.data.source.remote.network

import com.example.recipemate.data.source.remote.network.ApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitModule {
    private const val BASE_URL = "www.themealdb.com/api/json/v1/1/"
    const val SEARCH_END_POINT = "search.php"

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