package com.example.recipemate.ui.recipe.home

import com.example.recipemate.data.source.remote.model.Recipe

interface BookMarker {
    fun onBookmarkClicked(recipe: Recipe)
}