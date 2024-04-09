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
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentLocalProfileBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.homework_5.GetStubLocalUserUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LocalProfileFragment : Fragment() {

    private var _binding: FragmentLocalProfileBinding? = null
    private val binding: FragmentLocalProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding == null")

    private lateinit var viewModel: LocalProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocalProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stubLocalUserUseCase = GetStubLocalUserUseCase(requireActivity().applicationContext)
        viewModel = ViewModelProvider(
            this,
            LocalProfileViewModelFactory(stubLocalUserUseCase)
        )[LocalProfileViewModel::class.java]

        viewModel.screenState
            .flowWithLifecycle(lifecycle)
            .onEach(::render)
            .launchIn(lifecycleScope)

        binding.errorComponent.retryButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.setupStubData()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(newScreenState: LocalProfileScreenState) {
        when (newScreenState) {
            is LocalProfileScreenState.Content -> {
                with(binding) {
                    shimmerContainer.stopShimmer()
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    logOutButton.isVisible = true
                    userAvatarImage.isVisible = true
                    userName.isVisible = true
                    userOnlineStatus.isVisible = true
                    userActivityStatus.isVisible = true
                    userName.text = newScreenState.localUser.name
                    userAvatarImage.setImageBitmap(newScreenState.localUser.avatar)
                    userActivityStatus.text = newScreenState.localUser.activityStatus
                    userOnlineStatus.text = newScreenState.localUser.onlineStatus.name.lowercase()
                }
            }
            is LocalProfileScreenState.Error -> {
                with(binding) {
                    errorContainer.isVisible = true
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                    userActivityStatus.isVisible = false
                    logOutButton.isVisible = false
                }
            }
            is LocalProfileScreenState.Initial -> {
                with(binding) {
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                    userActivityStatus.isVisible = false
                    logOutButton.isVisible = false
                }
            }
            is LocalProfileScreenState.Loading -> {
                with(binding) {
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = true
                    shimmerContainer.startShimmer()
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                    userActivityStatus.isVisible = false
                    logOutButton.isVisible = false
                }

            }
        }
    }
}