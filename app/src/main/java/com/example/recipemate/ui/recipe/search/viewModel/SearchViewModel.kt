package com.example.recipemate.ui.recipe.search.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.data.source.remote.model.RecipeDetails
import kotlinx.coroutines.launch

class SearchViewModel(val recipeRepository: RecipeRepository) : ViewModel() {
    private val _recipes = MutableLiveData<List<RecipeDetails>>()
    val searchRecipes: LiveData<List<RecipeDetails>> = _recipes
    private val _status = MutableLiveData<String>()

    fun fetchSearchRecipes(query: String) {
        viewModelScope.launch {
            try {
                val response = recipeRepository.getSearchRecipes(query)
                _recipes.value = response
                _status.value = "Success"
                Log.e("TAG", "success to fetch search recipes: ")
            } catch (e: Exception) {
                _status.value = "Error ${e.message}"
                Log.e("TAG", "failed to fetch search recipes: ", e)
            }
        }
    }
}