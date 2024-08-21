package com.example.recipemate.ui.recipe.bookMark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.data.source.local.RecipeDatabase
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.databinding.FragmentBookMarkBinding
import com.example.recipemate.ui.recipe.home.BookMarker
import com.example.recipemate.ui.recipe.home.PopularAdapter


class BookMarkFragment : Fragment() {
    private var _binding: FragmentBookMarkBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : PopularAdapter

    private val bookMarkViewModel: BookMarkViewModel by viewModels {
        BookMarkViewModelFactory(
            RecipeRepository(
                RecipeDatabase.getInstance(requireContext()).recipeDao()
            )
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookMarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookMarkViewModel.getAllSavedRecipes()
         adapter = PopularAdapter( arrayListOf(),popularCommunicator,false,bookMarker)
        bookMarkViewModel.savedRecipes.observe(viewLifecycleOwner , Observer { value ->
          adapter.updateData(value)
            binding.recyclerViewBookmarkedRecipes.adapter = adapter
      })

    }
    private val popularCommunicator = object : PopularAdapter.Communicator {
        override fun onItemClicked(recipe: Recipe) {

        }
    }
    private val bookMarker = object  : BookMarker {
        override fun onBookmarkClick(recipe: Recipe) {
            bookMarkViewModel.chooseToAddOrDelete(recipe)
            bookMarkViewModel.getAllSavedRecipe()
        }

    }
}