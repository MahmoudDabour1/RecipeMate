package com.example.recipemate.ui.auth.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipemate.databinding.FragmentSplashBinding
import com.example.recipemate.ui.recipe.RecipeActivity
import com.example.recipemate.utils.SharedPrefUtils


class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.postDelayed({
            checkLoginStatus()
        }, 3000)
    }

    private fun checkLoginStatus() {

        val isLoggedIn = SharedPrefUtils.isLoggedIn(requireContext())

        if (isLoggedIn) {
            val intent = Intent(requireContext(), RecipeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        } else {

        }
    }

}