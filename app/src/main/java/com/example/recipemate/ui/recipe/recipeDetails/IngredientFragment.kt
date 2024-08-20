package com.example.recipemate.ui.recipe.recipeDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipemate.databinding.FragmentIngredientBinding

class IngredientFragment : Fragment() {
    private lateinit var binding: FragmentIngredientBinding
    private lateinit var adapter: IngredientAdaptor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ingredients = arguments?.getParcelableArrayList<Ingredient>(ARG_INGREDIENTS) ?: emptyList()
        adapter = IngredientAdaptor(ingredients)
        binding.recyclerIngredients.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerIngredients.adapter = adapter
    }

    companion object {
        private const val ARG_INGREDIENTS = "ingredients"

        fun newInstance(ingredients: List<Ingredient>): IngredientFragment {
            val fragment = IngredientFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_INGREDIENTS, ArrayList(ingredients))
            fragment.arguments = args
            return fragment
        }
    }
}