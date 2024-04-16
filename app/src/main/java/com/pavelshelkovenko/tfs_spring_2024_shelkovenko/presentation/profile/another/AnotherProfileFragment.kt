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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipApi
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipUserRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentAnotherProfileBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetAnotherProfileUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.setColoredTextStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AnotherProfileFragment : Fragment() {

    private var _binding: FragmentAnotherProfileBinding? = null
    private val binding: FragmentAnotherProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentAnotherProfileBinding == null")


    private val args by navArgs<AnotherProfileFragmentArgs>()

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

        val repository = ZulipUserRepository(ZulipApi())
        val useCase = GetAnotherProfileUseCase(repository)

        viewModel = ViewModelProvider(
            this,
            AnotherProfileViewModelFactory(useCase)
        )[AnotherProfileViewModel::class.java]


        viewModel.screenState
            .flowWithLifecycle(lifecycle)
            .onEach(::render)
            .launchIn(lifecycleScope)

        lifecycleScope.launch {
            viewModel.downloadData(args.userId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListeners() {
        with(binding) {
            errorComponent.retryButton.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.downloadData(args.userId)
                }
            }
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun render(newScreenState: AnotherProfileScreenState) {
        with(binding) {
            when (newScreenState) {
                is AnotherProfileScreenState.Content -> {
                    shimmerContainer.stopShimmer()
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = true
                    userName.isVisible = true
                    userOnlineStatus.isVisible = true
                    userName.text = newScreenState.anotherUser.name
                    userOnlineStatus.setColoredTextStatus(newScreenState.anotherUser.onlineStatus)
                    Glide.with(root).load(newScreenState.anotherUser.avatarUrl).into(userAvatarImage)
                }
                is AnotherProfileScreenState.Error -> {
                    errorContainer.isVisible = true
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                }
                AnotherProfileScreenState.Initial -> {
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                }
                AnotherProfileScreenState.Loading -> {
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = true
                    shimmerContainer.startShimmer()
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                }
            }
        }
    }
}