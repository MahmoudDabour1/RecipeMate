package com.example.recipemate.ui.recipe.recipeDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.recipemate.databinding.FragmentWatchVideoBinding

class WatchVideoFragment : Fragment() {
    lateinit var binding: FragmentWatchVideoBinding
    private val args: WatchVideoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWatchVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWatchVideoBinding.bind(view)
        val videoUrl = args.recipeYouTubeVideoUrl
        if (videoUrl.isNotEmpty()) {
            try {
                setupWebView(videoUrl)
            } catch (e: Exception) {
                Log.e("WatchVideoFragment", "Error loading video URL", e)
            }
        } else {
            Log.e("WatchVideoFragment", "Invalid video URL")
        }


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack()
                    } else {
                        findNavController().popBackStack()
                    }
                }
            })

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(url: String) {
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(url)
    }


}