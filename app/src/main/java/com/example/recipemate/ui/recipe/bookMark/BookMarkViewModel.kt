package com.example.recipemate.ui.recipe.bookMark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.data.source.remote.model.Recipe
import kotlinx.coroutines.launch

class BookMarkViewModel(val repo : RecipeRepository) : ViewModel() {
    private val _savedRecipes = MutableLiveData<List<Recipe>>()
    val savedRecipes : LiveData<List<Recipe>> = _savedRecipes

    fun getAllSavedRecipes() {
        viewModelScope.launch {
            _savedRecipes.value = repo.getAllFavRecipes()
        }
    }
    fun chooseToAddOrDelete(recipe: Recipe){
        viewModelScope.launch {
            if(recipe.isBookmarked) repo.addRecipeToFav(recipe)
            else repo.deleteRecipeFromFav(recipe)
        }
    }
}
