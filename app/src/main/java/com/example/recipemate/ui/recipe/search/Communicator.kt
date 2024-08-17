package com.example.recipemate.ui.recipe.search

import com.example.recipemate.data.source.remote.model.RecipeDetails

interface Communicator {
    fun onItemClicked(position: RecipeDetails)
}