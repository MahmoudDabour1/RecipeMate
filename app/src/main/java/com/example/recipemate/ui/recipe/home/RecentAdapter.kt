package com.example.recipemate.ui.recipe.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipemate.R
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.databinding.ItemRecentRecipesBinding

class RecentAdapter(
    private val recentRecipes: ArrayList<Recipe>,
    private val communicator: Communicator,
    private var isShimmer: Boolean,
    private val bookMarker: BookMarker,
    private var mySavedRecipes: List<Recipe>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class RecentViewHolder(private val binding: ItemRecentRecipesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.textViewRecentName.text = recipe.strMeal
            Glide.with(binding.imageRecentView.context).load(recipe.strMealThumb).placeholder(R.drawable.image_placeholder)
                .into(binding.imageRecentView)
            val bookmarkIcon = if (recipe.isBookmarked) {
                R.drawable.ic_bookmark_red
            } else {
                R.drawable.ic_bookmark
            }
            binding.imageViewFavorite.setImageResource(bookmarkIcon)
            binding.imageViewFavorite.setOnClickListener {
                recipe.isBookmarked = !recipe.isBookmarked
                notifyItemChanged(adapterPosition)
                bookMarker.onBookmarkClicked(recipe)
            }

            itemView.setOnClickListener {
                communicator.onItemClicked(recipe)
            }
        }
    }

    inner class ShimmerViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        return if (isShimmer) VIEW_TYPE_SHIMMER else VIEW_TYPE_CATEGORY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val view = inflater.inflate(R.layout.shimmer_recent_placeholder, parent, false)
            ShimmerViewHolder(view)
        } else {
            val binding = ItemRecentRecipesBinding.inflate(inflater, parent, false)
            RecentViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return if (isShimmer) SHIMMER_ITEM_COUNT else recentRecipes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecentViewHolder) {
            val currentItem = recentRecipes[position]
            holder.bind(currentItem)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Recipe>, isShimmer: Boolean, savedRecipe: List<Recipe>) {
        this.isShimmer = isShimmer
        recentRecipes.clear()
        recentRecipes.addAll(newItems)
        updataDataFromLocal(savedRecipe)
        notifyDataSetChanged()

    }

    fun updataDataFromLocal(recipes: List<Recipe>) {
        mySavedRecipes = recipes
        recentRecipes.forEach { recentRecipe ->
            recentRecipe.isBookmarked =
                mySavedRecipes.any { savedRecipe -> savedRecipe.idMeal == recentRecipe.idMeal }
        }
    }


    interface Communicator {
        fun onItemClicked(recipe: Recipe)
    }

    companion object {
        private const val SHIMMER_ITEM_COUNT = 5
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_CATEGORY = 1
    }
}
