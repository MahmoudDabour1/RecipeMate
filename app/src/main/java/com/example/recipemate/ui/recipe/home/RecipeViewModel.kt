package com.example.recipemate.ui.recipe.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.data.source.remote.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    private val _seafoodRecipes: MutableLiveData<ArrayList<Recipe>> = MutableLiveData()
    private val _status: MutableLiveData<String> = MutableLiveData()
    val seafoodRecipe: MutableLiveData<ArrayList<Recipe>> = _seafoodRecipes
    private val repository = RecipeRepository()

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> get() = _categories


    fun fetchRecipes() {
        viewModelScope.launch {
            try {
                _seafoodRecipes.value = repository.getSeafoodRecipes()
                _status.value = "Success"
                Log.e("TAG", "success to fetch search recipes: ")
            } catch (e: Exception) {
                _status.value = "Error ${e.message}"
                Log.e("TAG", "failed to fetch search recipes: ")

            }
        }
    }


    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val result = repository.fetchCategories()
                _categories.value = result
            } catch (e: Exception) {
                _status.value = "Error ${e.message}"
                Log.e("TAG", "failed to fetch categories: ")
            }
        }
    }
}