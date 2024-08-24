package com.example.recipemate.ui.recipe.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemate.R
import com.example.recipemate.data.source.remote.model.Category
import com.example.recipemate.databinding.ItemRowCategoryBinding

class CategoryAdapter(
    private val category: MutableList<Category>,
    private val communicator: Communicator,
    private var isShimmer: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedPosition: Int = 0

    inner class CategoryViewHolder(private val binding: ItemRowCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category, position: Int) {
            binding.textViewCategory.text = category.strCategory
            if (selectedPosition == position) {
                binding.textViewCategory.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.circular_background_category_selected
                )
                binding.textViewCategory.setTextColor(Color.WHITE)
            } else {
                binding.textViewCategory.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.category_item_background
                )
                binding.textViewCategory.setTextColor(Color.BLACK)
            }
            binding.root.setOnClickListener {
                communicator.onItemClick(category)
                val oldPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(oldPosition)
                notifyItemChanged(selectedPosition)
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
            val view = inflater.inflate(R.layout.shimmer_category_placeholder, parent, false)
            ShimmerViewHolder(view)
        } else {
            val binding =
                ItemRowCategoryBinding.inflate(inflater, parent, false)
            CategoryViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return if (isShimmer) SHIMMER_ITEM_COUNT else category.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_CATEGORY) {
            val categoryViewHolder = holder as CategoryViewHolder
            val currentItem = category[position]
            categoryViewHolder.bind(currentItem, position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Category>, isShimmer: Boolean) {
        this.isShimmer = isShimmer
        category.clear()
        category.addAll(newItems)
        notifyDataSetChanged()
    }


    interface Communicator {
        fun onItemClick(category: Category)
    }

    companion object {
        private const val SHIMMER_ITEM_COUNT = 5
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_CATEGORY = 1
    }
}
