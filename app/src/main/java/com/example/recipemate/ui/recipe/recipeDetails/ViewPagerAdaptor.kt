package com.example.recipemate.ui.recipe.recipeDetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdaptor(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val instructions: String
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IngredientFragment() // Assuming you have an IngredientsFragment
            1 -> InstructionFragment.newInstance(instructions)
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}