package com.example.recipemate.ui.recipe

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.recipemate.R
import com.example.recipemate.databinding.ActivityRecipeBinding
import com.example.recipemate.ui.recipe.home.RecipeViewModel

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.recipeDetailsFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.watchVideoFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }

    }
}