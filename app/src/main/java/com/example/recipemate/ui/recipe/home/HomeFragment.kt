package com.example.recipemate.ui.recipe.home

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemate.data.source.remote.model.Category
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var popularAdapter: PopularAdapter
    private lateinit var recentAdapter: RecentAdapter
    private lateinit var categoryAdapter: CategoryAdapter


    private lateinit var popularRecyclerView: RecyclerView
    private lateinit var recentRecyclerView: RecyclerView
    private lateinit var categoryRecyclerView: RecyclerView

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        popularRecyclerView = binding.popularRecipesRecyclerView
        recentRecyclerView = binding.recentRecipesRecyclerView
        categoryRecyclerView = binding.categoryRecyclerView

        popularAdapter = PopularAdapter(arrayListOf(), popularCommunicator)
        recentAdapter = RecentAdapter(arrayListOf(), recentCommunicator)
        categoryAdapter = CategoryAdapter(arrayListOf(), categoryCommunicator)

        popularRecyclerView.adapter = popularAdapter
        recentRecyclerView.adapter = recentAdapter
        categoryRecyclerView.adapter = categoryAdapter

        viewModel.popularRecipes.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                popularAdapter.updateData(it)

            }
        }

        viewModel.recentRecipes.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                recentAdapter.updateData(it)
            }
        }



        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categories?.let {
                categoryAdapter.updateData(it)
            }
        }
        viewModel.fetchRecipesByCategory("Beef")
        viewModel.fetchPopularRecipes()
        viewModel.fetchRecentRecipes()
        viewModel.fetchCategories()
        binding.textFieldSearchView.inputType = InputType.TYPE_NULL
        binding.textFieldSearchView.setOnClickListener {

        }
    }


    private val popularCommunicator = object : PopularAdapter.Communicator {
        override fun onItemClicked(position: Recipe) {

        }
    }
    private val recentCommunicator = object : RecentAdapter.Communicator {
        override fun onItemClicked(recipe: Recipe) {


        }
    }

    private val categoryCommunicator = object : CategoryAdapter.Communicator {
        override fun onItemClick(category: Category) {
            viewModel.fetchRecipesByCategory(category.strCategory ?: "Beef")
        }
    }


}


