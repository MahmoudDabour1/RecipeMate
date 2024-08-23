package com.example.recipemate.ui.recipe.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipemate.R
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.databinding.ItemPopularRecipeBinding


class PopularAdapter(
    private val seafoodRecipes: ArrayList<Recipe>,
    private val communicator: Communicator,
    private var isShimmer: Boolean,
    val bookMarker: BookMarker
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class RecipeViewHolder(private val binding: ItemPopularRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            val randomTime = (10..60).random()
            binding.textViewHomePopularName.text = recipe.strMeal
            binding.textViewHomePopularTime.text = "$randomTime mins"
            Glide.with(binding.imageViewRecipe.context).load(recipe.strMealThumb).placeholder(R.drawable.image_placeholder)
                .into(binding.imageViewRecipe)
            val bookmarkIcon = if (recipe.isBookmarked) {
                R.drawable.ic_bookmark_red
            } else {
                R.drawable.ic_bookmark_white
            }
            binding.imageViewBookmark.setImageResource(bookmarkIcon)
            binding.imageViewBookmark.setOnClickListener {
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
            val view = inflater.inflate(R.layout.shimmer_popular_placeholder, parent, false)
            ShimmerViewHolder(view)
        } else {
            val binding =
                ItemPopularRecipeBinding.inflate(inflater, parent, false)
            RecipeViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecipeViewHolder) {
            val currentItem = seafoodRecipes[position]
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return if (isShimmer) SHIMMER_ITEM_COUNT else seafoodRecipes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Recipe>, isShimmer: Boolean) {
        this.isShimmer = isShimmer
        seafoodRecipes.clear()
        seafoodRecipes.addAll(newItems)
        notifyDataSetChanged()
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
