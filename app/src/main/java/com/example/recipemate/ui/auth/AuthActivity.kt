package com.example.recipemate.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.recipemate.R
import com.example.recipemate.databinding.ActivityAuthBinding
import com.example.recipemate.utils.Constants

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        if (intent.getBooleanExtra(Constants.IS_LOGGED_OUT, false)) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.auth_nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }
}