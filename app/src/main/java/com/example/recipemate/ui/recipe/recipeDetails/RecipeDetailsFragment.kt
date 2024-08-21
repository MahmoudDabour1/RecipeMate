package com.example.recipemate.ui.recipe.recipeDetails

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.recipemate.R
import com.example.recipemate.data.source.remote.model.RecipeDetails
import com.example.recipemate.databinding.FragmentRecipeDetailsBinding
import com.example.recipemate.ui.recipe.recipeDetails.viewModel.RecipeDetailsViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class RecipeDetailsFragment : Fragment() {
    private lateinit var binding: FragmentRecipeDetailsBinding
    private val args: RecipeDetailsFragmentArgs by navArgs()
    private val viewModel: RecipeDetailsViewModel by viewModels()
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPagerAdapter: ViewPagerAdaptor
    private lateinit var ingredientAdapter: IngredientAdaptor
    private var recipeIngredients: List<Ingredient> = emptyList()
    private lateinit var recipeUrl: String
    private lateinit var recipeImage: String
    private var recipeInstructions: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        recipeImage = binding.recipeDetailsHeaderImageView.toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchRecipeDetails(args.recipeId)
        Log.e("TAG", "onViewCreated:  ${args.recipeId}")

        tabLayout = binding.tabLayoutRecipeDetails
        viewPager2 = binding.viewPagerRecipeDetails
        viewPagerAdapter =
            ViewPagerAdaptor(childFragmentManager, lifecycle, recipeInstructions, recipeIngredients)
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"))
        tabLayout.addTab(tabLayout.newTab().setText("Instructions"))
        viewPager2.adapter = viewPagerAdapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })




        viewModel.recipeDetails.observe(viewLifecycleOwner) { recipeDetails ->
            recipeDetails?.let {
                Log.e("RecipeDetailsFragment", "Observed details: $it")
                binding.textViewRecipeDetailsTitle.text = it[0].strMeal.toString()
                binding.textViewRecipeDetailsCategory.text = it[0].strCategory
                binding.textViewRecipeDetailsLocation.text = it[0].strArea
                Glide.with(binding.root)
                    .load(it[0].strMealThumb)
                    .into(binding.recipeDetailsHeaderImageView)
                recipeUrl = it[0].strYoutube.toString()
                recipeImage = it[0].strMealThumb.toString()
                recipeInstructions = it[0].strInstructions.toString()
                recipeIngredients = extractIngredients(it[0])
                ingredientAdapter = IngredientAdaptor(recipeIngredients)
                Log.e("extract", "onViewCreated:$recipeIngredients ")

                viewPagerAdapter =
                    ViewPagerAdaptor(
                        childFragmentManager,
                        lifecycle,
                        recipeInstructions,
                        recipeIngredients
                    )
                viewPager2.adapter = viewPagerAdapter

            } ?: run {
                Log.e("RecipeDetailsFragment", "Recipe details are null")
            }
        }

        handleOnClicks()
    }

    private fun handleOnClicks() {
        binding.imageViewRecipeDetailsBackArrow.setOnClickListener {
          findNavController().popBackStack()
        }

        binding.imageViewRecipeDetailsShare.setOnClickListener {
            shareRecipe()
        }

        binding.buttonRecipeDetailsWatchVideoView.setOnClickListener {
            val action =
                RecipeDetailsFragmentDirections.actionRecipeDetailsFragmentToWatchVideoFragment(
                    recipeUrl
                )
            findNavController().navigate(action)
        }
    }

    private fun shareRecipe() {
        lifecycleScope.launch {
            val sendIntent: String = "Recipe: ${binding.textViewRecipeDetailsTitle.text} \n" +
                    "Category: ${binding.textViewRecipeDetailsCategory.text} \n" +
                    "Location: ${binding.textViewRecipeDetailsLocation.text} \n" +
                    "YouTube: $recipeUrl"
            val imageUri = getLocalBitmapUri(recipeImage)

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, sendIntent)
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "image/*"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Share Recipe"))
        }
    }

    private suspend fun getLocalBitmapUri(imageUrl: String): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(requireContext().cacheDir, "shared_image.png")
                val outputStream = FileOutputStream(file)
                val bitmap = Glide.with(this@RecipeDetailsFragment)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
                FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.provider",
                    file
                )
            } catch (e: Exception) {
                Log.e("RecipeDetailsFragment", "Failed to get local bitmap URI", e)
                null
            }
        }
    }

    private fun extractIngredients(recipeDetails: RecipeDetails): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        for (i in 1..20) {
            val ingredientField = recipeDetails::class.java.getDeclaredField("strIngredient$i")
            val measureField = recipeDetails::class.java.getDeclaredField("strMeasure$i")
            ingredientField.isAccessible = true
            measureField.isAccessible = true
            val ingredient = ingredientField.get(recipeDetails) as? String
            val measure = measureField.get(recipeDetails) as? String
            if (!ingredient.isNullOrEmpty() && !measure.isNullOrEmpty()) {
                ingredients.add(Ingredient(ingredient, measure))
            }
        }
        return ingredients
    }
}