package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.local

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipApi
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.data.ZulipUserRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentOwnProfileBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetOwnProfileUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.setColoredTextStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class OwnProfileFragment : Fragment() {

    private var _binding: FragmentOwnProfileBinding? = null
    private val binding: FragmentOwnProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding == null")

    private lateinit var viewModel: OwnProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOwnProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Пока что у нас ручной Di)
        val repository = ZulipUserRepository(ZulipApi())
        val useCase = GetOwnProfileUseCase(repository)

        viewModel = ViewModelProvider(
            this,
            OwnProfileViewModelFactory(useCase)
        )[OwnProfileViewModel::class.java]

        viewModel.screenState
            .flowWithLifecycle(lifecycle)
            .onEach(::render)
            .launchIn(lifecycleScope)

        lifecycleScope.launch {
            viewModel.downloadData()
        }

        binding.errorComponent.retryButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.downloadData()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(newScreenState: OwnProfileScreenState) {
        with(binding) {
            when (newScreenState) {
                is OwnProfileScreenState.Content -> {
                    shimmerContainer.stopShimmer()
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = true
                    userName.isVisible = true
                    userOnlineStatus.isVisible = true
                    userName.text = newScreenState.ownUser.name
                    userOnlineStatus.setColoredTextStatus(status = newScreenState.ownUser.onlineStatus)
                    Glide.with(root).load(newScreenState.ownUser.avatarUrl).into(userAvatarImage)
                }
                is OwnProfileScreenState.Error -> {
                    errorContainer.isVisible = true
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                }
                is OwnProfileScreenState.Initial -> {
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                }
                is OwnProfileScreenState.Loading -> {
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