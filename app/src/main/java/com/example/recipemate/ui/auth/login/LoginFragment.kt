package com.example.recipemate.ui.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipemate.R
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.source.local.RecipeDatabase
import com.example.recipemate.databinding.FragmentLoginBinding
import com.example.recipemate.ui.recipe.RecipeActivity
import com.example.recipemate.utils.SharedPrefUtils


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            AuthRepository(
                RecipeDatabase.getInstance(requireContext()).userDao()
            )
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addCallBack()
        loginViewModel.loginResult.observe(viewLifecycleOwner) { registered ->
            if (registered) {
                SharedPrefUtils.setLoggedIn(requireContext(), true)
                navigateToRecipeActivity()
            }

        }
    }


    private fun addCallBack() {
        binding.apply {
            loginButton.setOnClickListener {
                val username = binding.usernameEditText.editText?.text.toString()
                val password = binding.passwordEditText.editText?.text.toString()
                if (validateInput(username, password)) {
                    loginViewModel.login(username, password)
                }
            }
            signUpButton.setOnClickListener {
                navigateToRegisterFragment()
            }
        }
    }

    private fun navigateToRegisterFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun validateInput(username: String, password: String): Boolean {
        return when {
            username.isEmpty() -> {
                binding.usernameEditText.error = "Username is required"
                false
            }

            password.isEmpty() -> {
                binding.passwordEditText.error = "Password is required"
                false
            }

            else -> true
        }

    }

    private fun navigateToRecipeActivity() {
        val sharedPreferences =
            requireContext().getSharedPreferences(SharedPrefUtils.PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(SharedPrefUtils.IS_LOGGED_IN, true).apply()

        val intent = Intent(requireContext(), RecipeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}