package com.example.recipemate.ui.recipe.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.data.source.remote.model.Category
import com.example.recipemate.data.source.remote.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(val recipeRepository: RecipeRepository, val authRepository: AuthRepository) :
    ViewModel() {

    private val _popularRecipes = MutableLiveData<List<Recipe>>()
    val popularRecipes: LiveData<List<Recipe>> get() = _popularRecipes

    private val _recentRecipes = MutableLiveData<List<Recipe>>()
    val recentRecipes: LiveData<List<Recipe>> get() = _recentRecipes


    private val _recipes: MutableLiveData<ArrayList<Recipe>> = MutableLiveData()
    private val _status: MutableLiveData<String> = MutableLiveData()

    val recipe: MutableLiveData<ArrayList<Recipe>> = _recipes
    val savedRecipes = MutableLiveData<List<Recipe>>()

    private var _toastMessage = MutableLiveData<String?>()


    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private val _currentUserEmail = MutableLiveData<String?>()
    val currentUserEmail: LiveData<String?> = _currentUserEmail

    init {
        findCurrentUserEmail()
    }

    fun fetchPopularRecipes() {
        viewModelScope.launch {
            try {
                val popularList = recipeRepository.getRecipe()
                _popularRecipes.postValue(popularList)
                _status.value = "Success fetching popular recipes"
            } catch (e: Exception) {
                _status.value = "Error fetching popular recipes: ${e.message}"
                Log.e("RecipeViewModel", "Error fetching popular recipes", e)
            }
        }
    }

    fun fetchRecipesByCategory(category: String) {
        viewModelScope.launch {
            try {
                val recipesByCategory = recipeRepository.fetchRecipesByCategory(category)
                _popularRecipes.postValue(recipesByCategory)
                _status.value = "Success fetching recipes by category"
            } catch (e: Exception) {
                _status.value = "Error fetching recipes by category: ${e.message}"
                Log.e("RecipeViewModel", "Error fetching recipes by category", e)
            }
        }
    }

    fun fetchRecentRecipes() {
        viewModelScope.launch {
            try {
                val recentList =
                    recipeRepository.fetchRecipesByCategory("Seafood") // Fixed category
                _recentRecipes.postValue(recentList)
                _status.value = "Success fetching recent recipes"
            } catch (e: Exception) {
                _status.value = "Error fetching recent recipes: ${e.message}"
                Log.e("RecipeViewModel", "Error fetching recent recipes", e)
            }
        }
    }


    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val result = recipeRepository.fetchCategories()
                _categories.value = result
            } catch (e: Exception) {
                _status.value = "Error ${e.message}"
                Log.e("TAG", "failed to fetch categories: ")
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
        }

    }

    fun getAllSavedRecipes() {
        viewModelScope.launch {
            savedRecipes.value =
                currentUserEmail.value?.let { recipeRepository.getAllFavRecipes(it) }
        }
    }

    fun findCurrentUserEmail() {
        viewModelScope.launch {
            _currentUserEmail.value = authRepository.findCurrentUser()?.email
        }
    }

    fun getToastMessage(): MutableLiveData<String?> = _toastMessage
    fun clearToastMessage() {
        _toastMessage.value = null
    }

}


