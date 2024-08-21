package com.example.recipemate.ui.recipe.bookMark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.ui.auth.login.LoginViewModel

class BookMarkViewModelFactory(private val repository: RecipeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookMarkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookMarkViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}