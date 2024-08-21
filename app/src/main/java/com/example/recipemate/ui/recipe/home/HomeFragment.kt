package com.example.recipemate.ui.recipe.home

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipemate.R
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.data.source.local.RecipeDatabase
import com.example.recipemate.data.source.remote.model.Category
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.databinding.FragmentHomeBinding
import com.example.recipemate.ui.recipe.bookMark.BookMarkViewModel
import com.example.recipemate.ui.recipe.bookMark.BookMarkViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var popularAdapter: PopularAdapter
    private lateinit var recentAdapter: RecentAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private var isShimmerCategory = true
    private var isShimmerRecent = true
    private var isShimmerPopular = true

    private val viewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(
            RecipeRepository(
                RecipeDatabase.getInstance(requireContext()).recipeDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupAdapters()
        setupRecyclerViews()
        setupSearchField()
        observeViewModel()
        fetchData()
    }

    private fun fetchData() {
        viewModel.apply {
            fetchRecipesByCategory(DEFAULT_CATEGORY)
            fetchRecentRecipes()
            fetchCategories()
        }
    }

    private fun setupSearchField() {
        binding.textFieldSearchView.inputType = InputType.TYPE_NULL
        binding.textFieldSearchView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun setupRecyclerViews() {
        binding.popularRecipesRecyclerView.adapter = popularAdapter
        binding.recentRecipesRecyclerView.adapter = recentAdapter
        binding.categoryRecyclerView.adapter = categoryAdapter
    }

    private fun setupAdapters() {
        popularAdapter =
            PopularAdapter(arrayListOf(), popularCommunicator, isShimmerPopular, bookMarker)
        recentAdapter = RecentAdapter(arrayListOf(), recentCommunicator, isShimmerRecent)
        categoryAdapter = CategoryAdapter(arrayListOf(), categoryCommunicator, isShimmerCategory)
    }

    private fun observeViewModel() {
        viewModel.popularRecipes.observe(viewLifecycleOwner) { recipes ->
            recipes?.let {
                isShimmerPopular = false
                popularAdapter.updateData(it, isShimmerPopular)
            }
        }

        viewModel.recentRecipes.observe(viewLifecycleOwner) { recipes ->
            recipes?.let {
                isShimmerRecent = false
                recentAdapter.updateData(it, isShimmerRecent)
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categories?.let {
                isShimmerCategory = false
                categoryAdapter.updateData(it, isShimmerCategory)
            }
        }
    }

    private val popularCommunicator = object : PopularAdapter.Communicator {
        override fun onItemClicked(recipe: Recipe) {
            val action =
                HomeFragmentDirections.actionHomeFragmentToRecipeDetailsFragment(recipe.idMeal.toString())
            findNavController().navigate(action)

        }
    }

    private val recentCommunicator = object : RecentAdapter.Communicator {
        override fun onItemClicked(recipe: Recipe) {
            val action =
                HomeFragmentDirections.actionHomeFragmentToRecipeDetailsFragment(recipe.idMeal.toString())
            findNavController().navigate(action)

        }
    }
    private val bookMarker = object : BookMarker {
        override fun onBookmarkClicked(recipe: Recipe) {
            viewModel.chooseToAddOrDelete(recipe)
        }

    }

    private val categoryCommunicator = object : CategoryAdapter.Communicator {
        override fun onItemClick(category: Category) {
            viewModel.fetchRecipesByCategory(category.strCategory ?: DEFAULT_CATEGORY)
        }
    }

    companion object {
        private const val DEFAULT_CATEGORY = "Beef"
    }
}
