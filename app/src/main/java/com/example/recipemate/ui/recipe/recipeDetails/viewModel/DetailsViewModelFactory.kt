package com.example.recipemate.ui.recipe.recipeDetails.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.repository.RecipeRepository

class DetailsViewModelFactory(
    private val recipeRepository: RecipeRepository,
    val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeDetailsViewModel(recipeRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}