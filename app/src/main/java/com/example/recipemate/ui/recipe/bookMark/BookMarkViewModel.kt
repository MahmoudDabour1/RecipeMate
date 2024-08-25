package com.example.recipemate.ui.recipe.bookMark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.data.source.remote.model.Recipe
import kotlinx.coroutines.launch

class BookMarkViewModel(
    val recipeRepository: RecipeRepository,
    val authRepository: AuthRepository
) : ViewModel() {
    private val _savedRecipes = MutableLiveData<List<Recipe>>()
    val savedRecipes: LiveData<List<Recipe>> = _savedRecipes
    val currentUserEmail = MutableLiveData<String>()

    fun getAllSavedRecipes() {
        viewModelScope.launch {
            currentUserEmail.value = authRepository.findCurrentUser()?.email
            _savedRecipes.value =
                currentUserEmail.value?.let { recipeRepository.getAllFavRecipes(it) }
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipe.isBookmarked = false
            recipeRepository.deleteRecipeFromFav(recipe)
            getAllSavedRecipes()
        }
    }

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            recipe.isBookmarked = true
            currentUserEmail.value?.let { recipeRepository.addRecipeToFav(recipe, it) }
            getAllSavedRecipes()
        }
    }

}
