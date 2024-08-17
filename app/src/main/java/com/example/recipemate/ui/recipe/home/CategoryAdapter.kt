package com.example.recipemate.ui.recipe.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemate.R
import com.example.recipemate.data.source.remote.model.Category
import com.example.recipemate.databinding.ItemRowCategoryBinding

class CategoryAdapter(
    private val category: MutableList<Category>,
    private val communicator: Communicator
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private var selectedPosition: Int = 0

    inner class CategoryViewHolder(private val binding: ItemRowCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category,position: Int) {
            binding.textViewCategory.text = category.strCategory
            if (selectedPosition == position) {
                binding.textViewCategory.background = ContextCompat.getDrawable(binding.root.context, R.drawable.circular_background_category_selected)
                binding.textViewCategory.setTextColor(Color.WHITE)
            } else {
                binding.textViewCategory.background = ContextCompat.getDrawable(binding.root.context, R.drawable.category_item_background)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemRowCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount() = category.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = category[position]
        holder.bind(currentItem,position)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Category>) {
        category.clear()
        category.addAll(newItems)
        notifyDataSetChanged()
    }

    interface Communicator {
         fun onItemClick(category: Category)
    }
}