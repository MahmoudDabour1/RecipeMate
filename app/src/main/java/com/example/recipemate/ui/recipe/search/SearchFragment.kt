package com.example.recipemate.ui.recipe.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemate.data.source.remote.model.RecipeDetails
import com.example.recipemate.databinding.FragmentSearchBinding
import com.example.recipemate.ui.recipe.search.viewModel.SearchViewModel

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchRecipesAdapter: RecipeSearchRecyclerAdaptor
    private lateinit var searchRecipesRecycler: RecyclerView
    private var isShimmer = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeViewModel()
    }

    private fun initUI() {
        searchRecipesRecycler = binding.recyclerViewSearchRecipes
        searchRecipesAdapter = RecipeSearchRecyclerAdapter(arrayListOf(), communicator,isShimmer)
        searchRecipesRecycler.adapter = searchRecipesAdapter
        binding.lottieAnimationEmptySearch.visibility = View.VISIBLE
        binding.recyclerViewSearchRecipes.visibility = View.GONE
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    binding.lottieAnimationEmptySearch.visibility = View.VISIBLE
                    binding.lottieAnimationNoDataFound.visibility = View.GONE
                    binding.recyclerViewSearchRecipes.visibility = View.GONE
                } else {
                    binding.lottieAnimationEmptySearch.visibility = View.GONE
                    isShimmer = true
                    binding.lottieAnimationEmptySearch.visibility = View.GONE
                    binding.recyclerViewSearchRecipes.visibility = View.VISIBLE
                    viewModel.fetchSearchRecipes(newText)
                }
                return false
            }
        })
    }

    private fun observeViewModel() {
        viewModel.searchRecipes.observe(viewLifecycleOwner) { recipes ->
            if (recipes.isNullOrEmpty()) {
                binding.lottieAnimationEmptySearch.visibility = View.GONE
                binding.lottieAnimationNoDataFound.visibility = View.VISIBLE
                binding.recyclerViewSearchRecipes.visibility = View.GONE
            } else {
                isShimmer = false
                searchRecipesAdapter.updateRecipes(recipes,isShimmer)
                binding.lottieAnimationEmptySearch.visibility = View.GONE
                binding.lottieAnimationNoDataFound.visibility = View.GONE
                binding.recyclerViewSearchRecipes.visibility = View.VISIBLE
            }
        }
    }

    private val communicator = object : Communicator {
        override fun onItemClicked(position: RecipeDetails) {
            val action =
                SearchFragmentDirections.actionSearchFragmentToRecipeDetailsFragment(position.idMeal.toString())
            findNavController().navigate(action)
        }
    }
}