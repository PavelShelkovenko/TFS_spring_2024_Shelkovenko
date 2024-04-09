package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.another

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentAnotherProfileBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5.GetStubAnotherUserUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AnotherProfileFragment : Fragment() {

    private var _binding: FragmentAnotherProfileBinding? = null
    private val binding: FragmentAnotherProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentAnotherProfileBinding == null")


    private lateinit var viewModel: AnotherProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnotherProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()

        val stubAnotherUserUseCase = GetStubAnotherUserUseCase(requireActivity().applicationContext)
        viewModel = ViewModelProvider(
            this,
            AnotherProfileViewModelFactory(stubAnotherUserUseCase)
        )[AnotherProfileViewModel::class.java]

        viewModel.screenState
            .flowWithLifecycle(lifecycle)
            .onEach(::render)
            .launchIn(lifecycleScope)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListeners() {
        with(binding) {
            errorComponent.retryButton.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.setupStubData()
                }
            }
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun render(newScreenState: AnotherProfileScreenState) {
        when (newScreenState) {
            is AnotherProfileScreenState.Content -> {
                with(binding) {
                    shimmerContainer.stopShimmer()
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = true
                    userName.isVisible = true
                    userOnlineStatus.isVisible = true
                    userActivityStatus.isVisible = true
                    userName.text = newScreenState.anotherUser.name
                    userAvatarImage.setImageBitmap(newScreenState.anotherUser.avatar)
                    userActivityStatus.text = newScreenState.anotherUser.activityStatus
                    userOnlineStatus.text = newScreenState.anotherUser.onlineStatus.name.lowercase()
                }
            }
            is AnotherProfileScreenState.Error -> {
                with(binding) {
                    errorContainer.isVisible = true
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                    userActivityStatus.isVisible = false
                }
            }
            AnotherProfileScreenState.Initial -> {
                with(binding) {
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                    userActivityStatus.isVisible = false
                }
            }
            AnotherProfileScreenState.Loading -> {
                with(binding) {
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = true
                    shimmerContainer.startShimmer()
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                    userActivityStatus.isVisible = false
                }
            }
        }
    }
}