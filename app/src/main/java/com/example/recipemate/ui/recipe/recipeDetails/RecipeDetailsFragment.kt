package com.example.recipemate.ui.recipe.recipeDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.recipemate.R
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.data.source.local.RecipeDatabase
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.data.source.remote.model.RecipeDetails
import com.example.recipemate.databinding.FragmentRecipeDetailsBinding
import com.example.recipemate.databinding.RecipeCategoryAndAreaLayoutBinding
import com.example.recipemate.databinding.RecipeHeaderLayoutBinding
import com.example.recipemate.ui.recipe.recipeDetails.viewModel.DetailsViewModelFactory
import com.example.recipemate.ui.recipe.recipeDetails.viewModel.RecipeDetailsViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class RecipeDetailsFragment : Fragment() {
    private lateinit var binding: FragmentRecipeDetailsBinding
    private val args: RecipeDetailsFragmentArgs by navArgs()
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPagerAdapter: ViewPagerAdaptor
    private lateinit var ingredientAdapter: IngredientAdaptor
    private var recipeIngredients: List<Ingredient> = emptyList()
    private lateinit var recipeUrl: String
    private lateinit var recipeImage: String
    private var recipeInstructions: String = ""
    private lateinit var recipeHeaderLayoutBinding: RecipeHeaderLayoutBinding
    private lateinit var recipeCategoryAndAreaLayoutBinding: RecipeCategoryAndAreaLayoutBinding
    private lateinit var lottieAnimationLoading: LottieAnimationView
    private lateinit var recipe: Recipe
    private val viewModel: RecipeDetailsViewModel by viewModels {
        val recipeDB = RecipeDatabase.getInstance(requireContext())
        DetailsViewModelFactory(
            RecipeRepository(
                recipeDB.recipeDao()
            ), AuthRepository(
                recipeDB.userDao()

            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingInflatedId", "InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lottieAnimationLoading = binding.lottieAnimationLoading
        lottieAnimationLoading.visibility = View.VISIBLE

        initUi()
        observeData()
        handleOnClicks()
        viewModel.fetchRecipeDetails(args.recipeId)
    }

    @SuppressLint("InflateParams")
    private fun initUi() {
        recipeHeaderLayoutBinding = binding.recipeDetailsHeaderLayout
        recipeCategoryAndAreaLayoutBinding = binding.recipeDetailsCategoryAndAreaLayout

        setUpTabLayout()
    }

    @SuppressLint("InflateParams")
    private fun setUpTabLayout() {
        tabLayout = recipeCategoryAndAreaLayoutBinding.tabLayoutRecipeDetails
        viewPager2 = recipeCategoryAndAreaLayoutBinding.viewPagerRecipeDetails
        ViewPagerAdaptor(
            childFragmentManager,
            lifecycle,
            recipeInstructions,
            recipeIngredients
        ).also { viewPagerAdapter = it }
        tabLayout.addTab(tabLayout.newTab().setText("Ingredients"))
        tabLayout.addTab(tabLayout.newTab().setText("Instructions"))
        viewPager2.adapter = viewPagerAdapter

        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                val customView = LayoutInflater.from(context).inflate(R.layout.tab_item_raw, null)
                val tabTextView = customView.findViewById<TextView>(R.id.tabTextView)
                tabTextView.text = tab.text
                tab.customView = customView
            }
        }

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

        viewModel.getToastMessage().observe(viewLifecycleOwner) { message ->
                message?.let {
                    view?.let { it1 -> Snackbar.make(it1, message, LENGTH_SHORT).show() }
                    viewModel.clearToastMessage()
                }
            }
        }

    private fun observeData() {
        viewModel.recipeDetails.observe(viewLifecycleOwner) { recipeDetails ->
            recipeDetails?.let {
                updateRecipeUI(it)
                lottieAnimationLoading.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
                binding.buttonRecipeDetailsWatchVideoView.visibility = View.VISIBLE
            } ?: run {
                lottieAnimationLoading.visibility = View.GONE
            }
        }
        viewModel.getToastMessage().observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateRecipeUI(it: List<RecipeDetails>) {
        recipe = Recipe(it[0].strMeal.toString(), it[0].strMealThumb, it[0].idMeal)
        recipeCategoryAndAreaLayoutBinding.textViewRecipeDetailsTitle.text =
            it[0].strMeal.toString()
        recipeCategoryAndAreaLayoutBinding.textViewRecipeDetailsCategory.text =
            it[0].strCategory
        recipeCategoryAndAreaLayoutBinding.textViewRecipeDetailsLocation.text =
            it[0].strArea
        Glide.with(binding.root)
            .load(it[0].strMealThumb)
            .into(recipeHeaderLayoutBinding.recipeDetailsHeaderImageView)
        recipeUrl = it[0].strYoutube.toString()
        recipeImage = it[0].strMealThumb.toString()
        recipeInstructions = it[0].strInstructions.toString()
        recipeIngredients = extractIngredients(it[0])
        ingredientAdapter = IngredientAdaptor(recipeIngredients)

        viewPagerAdapter =
            ViewPagerAdaptor(
                childFragmentManager,
                lifecycle,
                recipeInstructions,
                recipeIngredients
            )
        viewPager2.adapter = viewPagerAdapter
    }

    private fun handleOnClicks() {
        recipeHeaderLayoutBinding.imageViewRecipeDetailsBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        recipeHeaderLayoutBinding.imageViewRecipeDetailsShare.setOnClickListener {
            shareRecipe()
        }
        binding.buttonRecipeDetailsWatchVideoView.setOnClickListener {
            val action =
                RecipeDetailsFragmentDirections.actionRecipeDetailsFragmentToWatchVideoFragment(
                    recipeUrl
                )
            findNavController().navigate(action)
        }
        binding.recipeDetailsHeaderLayout.imageViewRecipeDetailsBookmark.setOnClickListener {
            if (this::recipe.isInitialized) {
                viewModel.addRecipeToFav(recipe)
            } else {
                Toast.makeText(context, "Recipe is not loaded yet.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareRecipe() {
        lifecycleScope.launch {
            val sendIntent: String =
                "Recipe: ${recipeCategoryAndAreaLayoutBinding.textViewRecipeDetailsTitle.text} \n" +
                        "Category: ${recipeCategoryAndAreaLayoutBinding.textViewRecipeDetailsCategory.text} \n" +
                        "Location: ${recipeCategoryAndAreaLayoutBinding.textViewRecipeDetailsLocation.text} \n" +
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
                val file = File(requireContext().cacheDir, "recipe_image.png")
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