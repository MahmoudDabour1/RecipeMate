package com.example.recipemate.ui.recipe.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.databinding.ItemRecentRecipesBinding
import com.example.recipemate.ui.recipe.home.PopularAdapter.Communicator

class RecentAdapter(
    private val recentRecipes: ArrayList<Recipe>,
    private val communicator: Communicator,
) :
    RecyclerView.Adapter<RecentAdapter.RecentViewHolder>() {


    inner class RecentViewHolder(private val binding: ItemRecentRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.textViewRecentName.text = recipe.strMeal
            Glide.with(binding.imageRecentView.context).load(recipe.strMealThumb)
                .into(binding.imageRecentView)
            itemView.setOnClickListener {
                communicator.onItemClicked(recipe)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val binding =
            ItemRecentRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentViewHolder(binding)
    }

    override fun getItemCount() = recentRecipes.size

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        val currentItem = recentRecipes[position]
        holder.bind(currentItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Recipe>) {
        recentRecipes.clear()
        recentRecipes.addAll(newItems)
        notifyDataSetChanged()
    }

    interface Communicator {
        fun onItemClicked(recipe: Recipe)
    }

}


/*binding.imageViewFavorite.setOnClickListener {
                recyclerInterface.onItemClick(adapterPosition)
                recipe.isSelected = !recipe.isSelected
                binding.imageViewFavorite.setImageResource(
                    if (recipe.isSelected) R.drawable.ic_bookmark_red else R.drawable.ic_bookmark_white
                )
            }*/

