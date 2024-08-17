package com.example.recipemate.ui.recipe.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipemate.data.source.remote.model.RecipeDetails
import com.example.recipemate.databinding.SearchItemRawBinding

class RecipeSearchRecyclerAdaptor(
    private var recipes: List<RecipeDetails>,
    private val communicator: Communicator
) : RecyclerView.Adapter<RecipeSearchRecyclerAdaptor.RecipeSearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeSearchViewHolder {
        val binding = SearchItemRawBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeSearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: RecipeSearchViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
        holder.itemView.setOnClickListener {
            communicator.onItemClicked(currentRecipe)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecipes(newRecipes: List<RecipeDetails>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    class RecipeSearchViewHolder(private val binding: SearchItemRawBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeDetails) {
            binding.textViewSearchRecipeName.text = recipe.strMeal
            Glide.with(itemView.context).load(recipe.strMealThumb)
                .into(binding.imageViewRecipeSearch)
        }

    }
}


