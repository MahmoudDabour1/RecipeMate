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


    private val _currentUserEmail = MutableLiveData<String>()
    val currentUserEmail: LiveData<String> = _currentUserEmail

    private val _bookmarkStatus = MutableLiveData<Boolean?>()
    val bookmarkStatus: LiveData<Boolean?> = _bookmarkStatus

    init {
        findCurrentUser()
    }

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

    fun chooseToAddOrDelete(recipe: Recipe) {
        viewModelScope.launch {
            val isInDatabase = _currentUserEmail.value?.let {
                recipe.idMeal?.let { it1 ->
                    recipeRepository.isRecipeInDatabase(
                        it1,
                        it
                    )
                }
            }
                ?: false
            if (!isInDatabase) {
                _currentUserEmail.value?.let { recipeRepository.addRecipeToFav(recipe, it) }
                recipeRepository.updateRecipes(recipe)
                _toastMessage.value = "Recipe has been successfully added!"
            } else {
                recipeRepository.deleteRecipeFromFav(recipe)
                recipeRepository.updateRecipes(recipe)
                _toastMessage.value = "Recipe has been successfully deleted!"
            }
            recipe.idMeal?.let { checkIfItemStored(it) }
        }

    }

    fun checkIfItemStored(recipeId: String) {
        _currentUserEmail.value?.let { email ->
            viewModelScope.launch {
                val isInDatabase = recipeRepository.isRecipeInDatabase(recipeId, email)
                if (isInDatabase) {
                    _bookmarkStatus.value = true
                } else {
                    _bookmarkStatus.value = false
                }
            }
        }
    }

    fun findCurrentUser() {
        viewModelScope.launch {
            _currentUserEmail.value = authRepository.findCurrentUser()?.email
        }
    }

    fun getToastMessage(): LiveData<String?> = _toastMessage
    fun clearToastMessage() {
        _toastMessage.value = null
    }
}



