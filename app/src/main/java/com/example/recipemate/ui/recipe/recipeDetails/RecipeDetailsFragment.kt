package com.example.recipemate.ui.recipe.recipeDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipemate.databinding.FragmentRecipeDetailsBinding
import com.example.recipemate.ui.recipe.recipeDetails.viewModel.RecipeDetailsViewModel

class RecipeDetailsFragment : Fragment() {
    private lateinit var binding: FragmentRecipeDetailsBinding
    private val args: RecipeDetailsFragmentArgs by navArgs()
    private val viewModel: RecipeDetailsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchRecipeDetails(args.recipeId)
        Log.e("TAG", "onViewCreated:  ${args.recipeId}")


        viewModel.recipeDetails.observe(viewLifecycleOwner) { recipeDetails ->
            recipeDetails?.let {
                Log.e("RecipeDetailsFragment", "Observed details: $it")
                binding.textViewRecipeDetailsTitle.text = it[0].strMeal.toString()
                binding.textViewRecipeDetailsCategory.text = it[0].strCategory
                binding.textViewRecipeDetailsLocation.text = it[0].strArea
                binding.textViewRecipeDetailsDescriptionIngredientOrInstructions.text =
                    it[0].strInstructions
                Glide.with(binding.root)
                    .load(it[0].strMealThumb)
                    .into(binding.recipeDetailsHeaderImageView)
            } ?: run {
                Log.e("RecipeDetailsFragment", "Recipe details are null")
            }
        }
    }
}