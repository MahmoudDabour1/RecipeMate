package com.example.recipemate.ui.recipe.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.repository.RecipeRepository

class RecipeViewModelFactory(
    private val recipeRepository: RecipeRepository,
    private val authRepository: AuthRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(recipeRepository, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}