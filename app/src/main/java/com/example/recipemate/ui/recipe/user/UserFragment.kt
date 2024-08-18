package com.example.recipemate.ui.recipe.user

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.recipemate.R
import com.example.recipemate.data.repository.AuthRepository
import com.example.recipemate.data.source.local.RecipeDatabase
import com.example.recipemate.data.source.local.User
import com.example.recipemate.databinding.FragmentUserBinding
import com.example.recipemate.ui.auth.AuthActivity
import com.example.recipemate.utils.Constants.Companion.IS_LOGGED_OUT
import com.example.recipemate.utils.Constants.Companion.PICK_IMAGE_CODE
import com.example.recipemate.utils.Constants.Companion.REQUEST_PERMISSION_CODE
import com.example.recipemate.utils.SharedPrefUtils


class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            AuthRepository(
                RecipeDatabase.getInstance(requireContext()).userDao()
            )
        )
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
        userViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            if (it != null) getCurrentUserData(it)
        })
    }

    private fun changeProfilePicture() {
        binding.btnEditPhoto.setOnClickListener {
            requestStoragePermission()
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  // Android 13 (API 33) and above
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    REQUEST_PERMISSION_CODE
                )
            } else {
                openGallery()
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_CODE
                )
            } else {
                openGallery()
            }
        } else {
            openGallery()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(activity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageUri?.let {
                Glide.with(this).load(it).into(binding.userImage)
                userViewModel.updateUserImage(it)

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showSignOutDialog() {
        binding.tvSignOut.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.custom_sign_out_dialog, null)
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder
                .setMessage("Are you sure you want to sign out?")
                .setTitle("Sign Out")
                .setIcon(R.drawable.ic_alert)
                .setView(dialogView)
            val dialog: AlertDialog = builder.create()
            dialogView.findViewById<Button>(R.id.yesButton).setOnClickListener {
                signOut()
                dialog.dismiss()
            }
            dialogView.findViewById<Button>(R.id.cancelButton).setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    fun signOut() {
        val sharedPreferences =
            requireContext().getSharedPreferences(SharedPrefUtils.PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(SharedPrefUtils.IS_LOGGED_IN, false).apply()
        userViewModel.signOutCurrentUser()
        val intent = Intent(activity, AuthActivity::class.java)
        intent.putExtra(IS_LOGGED_OUT, true)
        activity?.finish()
        startActivity(intent)

    }

    fun getCurrentUserData(user: User) {
        binding.tvUserName.text = getString(R.string.full_user_name, user.firstName, user.lastName)
        binding.tvEmailAddress.text = user.email
        binding.tvPhoneNumber.text = user.phone
        if (user.isMale) binding.tvGenderValue.text = getString(R.string.male)
        else binding.tvGenderTitle.text = getString(R.string.female)
        if(user.imageUri!=null) Glide.with(this).load(user.imageUri).into(binding.userImage)


    }
}
