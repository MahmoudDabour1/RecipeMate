package com.example.recipemate.ui.recipe.search

import com.example.recipemate.data.source.RecipeDetails

interface Communicator {
    fun onItemClicked(position: RecipeDetails)
}