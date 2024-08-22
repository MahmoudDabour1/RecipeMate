package com.example.recipemate.ui.recipe.bookMark

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.databinding.ItemSavedItemBinding
import com.example.recipemate.ui.recipe.home.BookMarker

class BookMarkerAdapter(
    private val savedRecipes: ArrayList<Recipe>,
    private val communicator: Communicator,
    val bookMarker: BookMarker
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class RecipeViewHolder(private val binding: ItemSavedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.tvSavedRecipeName.text = recipe.strMeal
            Glide.with(binding.imageFav.context).load(recipe.strMealThumb)
                .into(binding.imageFav)
            binding.imgBookMark.setOnClickListener {
                recipe.isBookmarked = !recipe.isBookmarked
                notifyItemChanged(adapterPosition)
                bookMarker.onBookmarkClicked(recipe)

            }

            itemView.setOnClickListener {
                communicator.onItemClicked(recipe)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            ItemSavedItemBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding)

    }


    override fun getItemCount(): Int {
        return savedRecipes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BookMarkerAdapter.RecipeViewHolder) {
            val currentItem = savedRecipes[position]
            holder.bind(currentItem)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Recipe>) {
        savedRecipes.clear()
        savedRecipes.addAll(newItems)
        savedRecipes.reverse()
        notifyDataSetChanged()
    }


    interface Communicator {
        fun onItemClicked(recipe: Recipe)
    }

}
