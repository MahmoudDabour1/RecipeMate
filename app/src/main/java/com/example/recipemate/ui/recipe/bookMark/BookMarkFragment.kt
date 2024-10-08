package com.example.recipemate.ui.recipe.bookMark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.recipemate.R
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.repository.RecipeRepository
import com.example.recipemate.data.source.local.RecipeDatabase
import com.example.recipemate.data.source.remote.model.Recipe
import com.example.recipemate.databinding.FragmentBookMarkBinding
import com.example.recipemate.ui.recipe.home.BookMarker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class BookMarkFragment : Fragment() {
    private var _binding: FragmentBookMarkBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BookMarkerAdapter

    private val bookMarkViewModel: BookMarkViewModel by viewModels {
        val recipeDB = RecipeDatabase.getInstance(requireContext())
        BookMarkViewModelFactory(
            RecipeRepository(
                recipeDB.recipeDao()
            ), AuthRepository(
                recipeDB.userDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookMarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        bookMarkViewModel.getAllSavedRecipes()
    }

    private fun setupRecyclerView() {
        adapter = BookMarkerAdapter(arrayListOf(), savedCommunicator, bookMarker)
        binding.recyclerViewBookmarkedRecipes.adapter = adapter
    }

    private fun observeViewModel() {
        bookMarkViewModel.savedRecipes.observe(viewLifecycleOwner, Observer { recipes ->
            if (recipes.isEmpty()) {
                binding.lottieAnimationEmptySearch.visibility = View.VISIBLE
                binding.recyclerViewBookmarkedRecipes.visibility = View.GONE
            } else {
                binding.lottieAnimationEmptySearch.visibility = View.GONE
                binding.recyclerViewBookmarkedRecipes.visibility = View.VISIBLE
                adapter.updateData(recipes)
            }
        })
    }

    private fun showConfirmationDialog(recipe: Recipe) {
        MaterialAlertDialogBuilder(requireContext(), R.style.myDialog)
            .setTitle(R.string.confirm_title)
            .setMessage(R.string.confirm_message)
            .setIcon(R.drawable.ic_delete)
            .setCancelable(true)
            .setPositiveButton(R.string.yes) { _, _ ->
                bookMarkViewModel.deleteRecipe(recipe)
                showSnackBar(recipe)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    fun showSnackBar(recipe: Recipe) {
        view?.let {
            Snackbar.make(it, "Successfully deleted recipe", Snackbar.LENGTH_SHORT)
                .setAction("UNDO") {
                    bookMarkViewModel.addRecipe(recipe)
                }.setActionTextColor(resources.getColor(R.color.primaryColor)).show()

        }
    }


    private val savedCommunicator = object : BookMarkerAdapter.Communicator {
        override fun onItemClicked(recipe: Recipe) {
            val action =
                BookMarkFragmentDirections.actionBookMarkFragmentToRecipeDetailsFragment(recipe.idMeal.toString())
            findNavController().navigate(action)
        }
    }

    private val bookMarker = object : BookMarker {
        override fun onBookmarkClicked(recipe: Recipe) {
            showConfirmationDialog(recipe)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}