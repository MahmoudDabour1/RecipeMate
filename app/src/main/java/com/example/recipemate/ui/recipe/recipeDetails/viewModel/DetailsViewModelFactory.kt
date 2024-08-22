package com.example.recipemate.ui.recipe.recipeDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.ui.recipe.home.RecipeViewModel

class DetailsViewModelFactory (private val repository: RecipeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}