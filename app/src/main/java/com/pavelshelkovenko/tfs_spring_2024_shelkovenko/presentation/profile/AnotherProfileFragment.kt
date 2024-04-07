package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentAnotherProfileBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.chat.ChatFragmentArgs

class AnotherProfileFragment : Fragment() {

    private var _binding: FragmentAnotherProfileBinding? = null
    private val binding: FragmentAnotherProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentAnotherProfileBinding == null")


    private val args by navArgs<AnotherProfileFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnotherProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userName.text = args.userName

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}