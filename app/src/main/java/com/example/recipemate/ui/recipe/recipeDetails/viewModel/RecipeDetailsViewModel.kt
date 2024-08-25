package com.example.recipemate.ui.recipe.recipeDetails.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.data.source.remote.model.RecipeDetails
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(
    val recipeRepository: RecipeRepository,
    val authRepository: AuthRepository
) : ViewModel() {
    private val _recipeDetails = MutableLiveData<List<RecipeDetails>>()
    val recipeDetails: LiveData<List<RecipeDetails>> = _recipeDetails

    private var _toastMessage = MutableLiveData<String?>()
    private val _status = MutableLiveData<String>()

    private val currentUserEmail = MutableLiveData<String>()

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

    fun addRecipeToFav(recipe: Recipe) {
        viewModelScope.launch {
            currentUserEmail.value = authRepository.findCurrentUser()?.email
            val isInDatabase = currentUserEmail.value?.let {
                recipeRepository.isRecipeInDatabase(
                    recipe,
                    it
                )
            }
                ?: false
            if (!isInDatabase) {
                currentUserEmail.value?.let { recipeRepository.addRecipeToFav(recipe, it) }
                recipeRepository.updateRecipes(recipe)
                recipe.isBookmarked = !recipe.isBookmarked
                _toastMessage.value = "Recipe has been successfully added!"
            } else {
                _toastMessage.value = "This recipe has already been added!"
            }
        }

    }

    fun getToastMessage(): LiveData<String?> = _toastMessage
    fun clearToastMessage() {
        _toastMessage.value = null
    }


}