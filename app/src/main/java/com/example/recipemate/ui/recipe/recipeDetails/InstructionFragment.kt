package com.example.recipemate.ui.recipe.recipeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipemate.databinding.FragmentInstructionBinding

class InstructionFragment : Fragment() {
    private lateinit var binding: FragmentInstructionBinding
    private var instructions: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            instructions = it.getString(ARG_INSTRUCTIONS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstructionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewInstructions.text = instructions
    }

    companion object {
        private const val ARG_INSTRUCTIONS = "instructions"

        fun newInstance(instructions: String) =
            InstructionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_INSTRUCTIONS, instructions)
                }
            }
    }
}