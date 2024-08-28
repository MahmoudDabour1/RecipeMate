package com.example.recipemate.ui.recipe.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipemate.R
import com.example.recipemate.data.source.remote.model.RecipeDetails
import com.example.recipemate.databinding.SearchItemRawBinding

class RecipeSearchAdapter(
    private var recipes: List<RecipeDetails>,
    private val communicator: com.example.recipemate.ui.recipe.search.Communicator,
    private var isShimmer: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val view = inflater.inflate(R.layout.shimmer_search_placeholder, parent, false)
            ShimmerViewHolder(view)
        } else {
            val binding = SearchItemRawBinding.inflate(inflater, parent, false)
            RecipeSearchViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isShimmer) VIEW_TYPE_SHIMMER else VIEW_TYPE_CATEGORY
    }

    override fun getItemCount(): Int {
        return if (isShimmer) SHIMMER_ITEM_COUNT else recipes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecipeSearchViewHolder && !isShimmer) {
            val currentRecipe = recipes[position]
            holder.bind(currentRecipe)
            holder.itemView.setOnClickListener {
                communicator.onItemClicked(currentRecipe)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecipes(newRecipes: List<RecipeDetails>, isShimmer: Boolean) {
        this.isShimmer = isShimmer
        recipes = newRecipes
        notifyDataSetChanged()
    }

    inner class ShimmerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class RecipeSearchViewHolder(private val binding: SearchItemRawBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeDetails) {
            binding.textViewSearchRecipeName.text = recipe.strMeal
            Glide.with(itemView.context).load(recipe.strMealThumb)
                .placeholder(R.drawable.recipe_placeholder)
                .into(binding.imageViewRecipeSearch)
        }
    }

    companion object {
        private const val SHIMMER_ITEM_COUNT = 10
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_CATEGORY = 1
    }

    interface Communicator {
        fun onItemClicked(recipe: RecipeDetails)
    }
}