package com.example.recipemate.ui.recipe.user

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.recipemate.R
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.source.local.RecipeDatabase
import com.example.recipemate.data.source.local.User
import com.example.recipemate.databinding.CustomPasswordDialogBinding
import com.example.recipemate.databinding.FragmentUserBinding
import com.example.recipemate.ui.auth.AuthActivity
import com.example.recipemate.utils.Constants.Companion.IS_LOGGED_OUT
import com.example.recipemate.utils.SharedPrefUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            AuthRepository(
                RecipeDatabase.getInstance(requireContext()).userDao()
            )
        )
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { imageUri ->
                    Glide.with(this).load(imageUri).into(binding.userImage)
                    userViewModel.updateUserImage(imageUri)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeProfilePicture()
        showSignOutDialog()
        clickOnEditPass()
        userViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                getCurrentUserData(it)
                user = it
            }
        })
    }

    private fun changeProfilePicture() {
        binding.btnEditPhoto.setOnClickListener {
            requestStoragePermission()
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        resultLauncher.launch(intent)
    }

    private fun showSignOutDialog() {
        binding.tvSignOut.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.sign_out_title)
                .setMessage(R.string.sign_out_confirmation_message)
                .setIcon(R.drawable.ic_alert)
                .setCancelable(true)
                .setPositiveButton(R.string.yes) { _, _ ->
                    signOut()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

    }


    private fun signOut() {
        requireContext().getSharedPreferences(SharedPrefUtils.PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(SharedPrefUtils.IS_LOGGED_IN, false)
            .apply()
        userViewModel.signOutCurrentUser()
        startActivity(Intent(activity, AuthActivity::class.java).apply {
            putExtra(IS_LOGGED_OUT, true)
        })
        activity?.finish()
    }

    private fun getCurrentUserData(user: User) {
        binding.tvUserName.text = getString(R.string.full_user_name, user.firstName, user.lastName)
        binding.tvEmailAddress.text = user.email
        binding.tvPhoneNumber.text = user.phone
        binding.tvGenderValue.text =
            if (user.isMale) getString(R.string.male) else getString(R.string.female)
        user.imageUri?.let { Glide.with(this).load(it).into(binding.userImage) }
    }

    private fun clickOnEditPass() {
        binding.imgUpdatePass.setOnClickListener {
            showPassDialog()
        }
    }

    private fun validatePassword(
        binding: CustomPasswordDialogBinding,
        dialog: androidx.appcompat.app.AlertDialog
    ) {
        val currentPass = binding.currentPasswordEditText.editText?.text.toString()
        val newPass = binding.newPasswordEditText.editText?.text.toString()
        val confirmPass = binding.confirmPasswordEditText.editText?.text.toString()

        clearError(binding)

        when {
            currentPass != user.password -> {
                binding.currentPasswordEditText.error = getString(R.string.error_current_password)
            }

            newPass.isEmpty() -> {
                binding.newPasswordEditText.error = getString(R.string.error_empty_new_password)
            }

            newPass.length < 8 -> {
                binding.newPasswordEditText.error = getString(R.string.error_short_password)
            }

            newPass == currentPass -> {
                binding.newPasswordEditText.error =
                    getString(R.string.error_same_as_current_password)
            }

            newPass != confirmPass -> {
                binding.confirmPasswordEditText.error = getString(R.string.error_password_mismatch)
            }

            else -> {
                dialog.dismiss()
                userViewModel.updateUserPass(newPass)
                Toast.makeText(
                    requireContext(),
                    R.string.password_updated_success,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showPassDialog() {
        val dialogBinding = CustomPasswordDialogBinding.inflate(LayoutInflater.from(context))
        val materialDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialogBinding.changePasswordButton.setOnClickListener {
            validatePassword(dialogBinding, materialDialog)
        }

        materialDialog.show()
    }

    private fun clearError(binding: CustomPasswordDialogBinding) {
        binding.currentPasswordEditText.error = null
        binding.newPasswordEditText.error = null
        binding.confirmPasswordEditText.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
