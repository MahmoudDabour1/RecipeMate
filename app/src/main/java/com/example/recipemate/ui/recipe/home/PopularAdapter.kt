package com.example.recipemate.ui.recipe.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.databinding.ItemPopularRecipeBinding

class PopularAdapter(
    private val seafoodRecipes: ArrayList<Recipe>,
    private val communicator: Communicator
) :
    RecyclerView.Adapter<PopularAdapter.MealsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder {
        val binding =
            ItemPopularRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealsViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
        val currentItem = seafoodRecipes[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = seafoodRecipes.size


    inner class MealsViewHolder(private val binding: ItemPopularRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.textViewHomePopularName.text = recipe.strMeal
            binding.textViewHomePopularTime.text = "20 mins"
            Glide.with(binding.imageViewRecipe.context).load(recipe.strMealThumb)
                .into(binding.imageViewRecipe)
            itemView.setOnClickListener {
                communicator.onItemClicked(recipe)
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Recipe>) {
        seafoodRecipes.clear()
        seafoodRecipes.addAll(newItems)
        notifyDataSetChanged()
    }

    interface Communicator {
        fun onItemClicked(recipe: Recipe)
    }
}