package com.example.recipemate.ui.recipe.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var popularAdapter: PopularAdapter
    private lateinit var recentAdapter: RecentAdapter
    private lateinit var popularRecyclerView: RecyclerView
    private lateinit var recentRecyclerView: RecyclerView
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
        popularAdapter = PopularAdapter(arrayListOf(), popularCommunicator)
        recentAdapter = RecentAdapter(arrayListOf(),recentCommunicator )
        popularRecyclerView.adapter = popularAdapter
        recentRecyclerView.adapter = recentAdapter
        viewModel.seafoodRecipe.observe(viewLifecycleOwner) { recipe ->
            recipe?.let {
                popularAdapter.updateData(it)
                recentAdapter.updateData(it)
            }
        }
        viewModel.fetchRecipes()

    }

    private val popularCommunicator = object : PopularAdapter.Communicator {
        override fun onItemClicked(position: Recipe) {

        }
    }
    private val recentCommunicator = object : RecentAdapter.Communicator {
        override fun onItemClicked(position: Recipe) {

        }
    }

}


