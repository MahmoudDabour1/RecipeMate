package com.example.recipemate.ui.recipe.recipeDetails.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.data.source.remote.model.RecipeDetails
import kotlinx.coroutines.launch

class RecipeDetailsViewModel : ViewModel() {
    private val recipeRepository = RecipeRepository()
    private val _recipeDetails = MutableLiveData<List<RecipeDetails>>()
    val recipeDetails: LiveData<List<RecipeDetails>> = _recipeDetails


    private val _status = MutableLiveData<String>()

    fun fetchRecipeDetails(id: String) {
        viewModelScope.launch {
            try {
                val details = recipeRepository.fetchRecipeDetails(id)
                Log.e("RecipeDetailsViewModel", "Fetched details: $details")
                _recipeDetails.value = details
                _status.value = "Success"
                Log.e("RecipeDetails", "success to fetch recipe details: ")
            } catch (e: Exception) {
                _status.value = "Error ${e.message}"
                Log.e("RecipeDetails", "failed to fetch recipe details: ", e)
            }
        }
    }
}