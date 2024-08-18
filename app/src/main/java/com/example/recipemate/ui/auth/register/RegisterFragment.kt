package com.example.recipemate.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipemate.R
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.source.local.RecipeDatabase
import com.example.recipemate.databinding.FragmentRegisterBinding
import com.example.recipemate.utils.Validation


class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory(
            AuthRepository(
                RecipeDatabase.getInstance(requireContext()).userDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addCallBack()
    }

    private fun addCallBack() {
        binding.signUpButton.setOnClickListener {
            clearErrors()
            val email = binding.emailEditText.editText?.text.toString()
            val password = binding.passwordEditText.editText?.text.toString()
            val firstName = binding.firstNameEditText.editText?.text.toString()
            val lastName = binding.lastNameEditText.editText?.text.toString()
            val phoneNumber = binding.phoneNumberEditText.editText?.text.toString()
            val isMale = binding.maleRadioButton.isChecked
            if (Validation.validateInput(email, password, firstName, lastName, phoneNumber)) {

                checkEmail(email, password, firstName, lastName, phoneNumber, isMale)

            } else {
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches()
                ) {
                    binding.emailEditText.error = "Invalid email address"
                }

                if (password.isEmpty() || password.length < 6 || !password.any { it.isDigit() }) {
                    binding.passwordEditText.error =
                        "Password must contain at least 6 characters and include a number"
                }

                if (firstName.isEmpty() || !firstName.matches(Regex("^[A-Za-z]+$"))) {
                    binding.firstNameEditText.error = "First name must contain only letters"
                }

                if (lastName.isEmpty() || !lastName.matches(Regex("^[A-Za-z]+$"))) {
                    binding.lastNameEditText.error = "Last name must contain only letters"
                }

                if (phoneNumber.isEmpty() || !phoneNumber.matches(Regex("^\\+?[0-9]{10,15}$"))) {
                    binding.phoneNumberEditText.error = "Invalid phone number"
                }
            }
        }
    }

    private fun checkEmail(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        isMale: Boolean
    ) {
        registerViewModel.checkIfEmailRegistered(email)
        registerViewModel.userEmailRegistered.observe(viewLifecycleOwner) { registered ->
            if (registered) {
                binding.emailEditText.error = "Email already registered"
            } else {
                register(email, password, firstName, lastName, phoneNumber, isMale)
            }

        }
    }

    private fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        isMale: Boolean
    ) {
        registerViewModel.register(
            email,
            password,
            firstName,
            lastName,
            phoneNumber,
            isMale
        )
        registerViewModel.registrationStatus.observe(viewLifecycleOwner) { registered ->
            if (registered) {
                navigateToLoginFragment()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Registration failed. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun navigateToLoginFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    private fun clearErrors() {
        binding.emailEditText.error = null
        binding.passwordEditText.error = null
        binding.firstNameEditText.error = null
        binding.lastNameEditText.error = null
        binding.phoneNumberEditText.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

