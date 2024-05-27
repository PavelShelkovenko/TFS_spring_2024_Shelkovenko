package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentOwnProfileBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.ElmBaseFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.getApplication
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.setColoredTextStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.showErrorToast
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class OwnProfileFragment :
    ElmBaseFragment<OwnProfileEffect, OwnProfileState, OwnProfileEvent>(R.layout.fragment_own_profile) {

    private val binding: FragmentOwnProfileBinding by viewBinding(FragmentOwnProfileBinding::bind)

    @Inject
    lateinit var ownProfileStoreFactory: OwnProfileStoreFactory

    override fun onAttach(context: Context) {
        context.getApplication
            .appComponent
            .ownProfileComponent()
            .inject(this)
        super.onAttach(context)
    }

    override val store: Store<OwnProfileEvent, OwnProfileEffect, OwnProfileState> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        ownProfileStoreFactory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            store.accept(OwnProfileEvent.Ui.StartProcess)
        }
        setupClickListeners()
    }

    override fun handleEffect(effect: OwnProfileEffect) {
        when(effect) {
            is OwnProfileEffect.MinorError -> { showErrorToast(effect.errorMessageId, requireActivity()) }
        }
    }


    override fun render(state: OwnProfileState) {
        with(binding) {
            when (state) {
                is OwnProfileState.Content -> {
                    shimmerContainer.isVisible = false
                    errorContainer.isVisible = false
                    ownUserAvatarImage.isVisible = true
                    ownUserName.isVisible = true
                    ownUserOnlineStatus.isVisible = true
                    settingsButton.isVisible = true
                    shimmerContainer.stopShimmer()
                    ownUserName.text = state.ownUser.name
                    ownUserOnlineStatus.setColoredTextStatus(status = state.ownUser.onlineStatus)
                    Glide.with(root).load(state.ownUser.avatarUrl).into(ownUserAvatarImage)
                }

                is OwnProfileState.Error -> {
                    errorContainer.isVisible = true
                    shimmerContainer.isVisible = false
                    ownUserAvatarImage.isVisible = false
                    ownUserName.isVisible = false
                    ownUserOnlineStatus.isVisible = false
                    settingsButton.isVisible = false
                    errorComponent.errorMessage.text = resources.getString(state.errorMessageId)
                    shimmerContainer.stopShimmer()
                }

                is OwnProfileState.Initial -> {
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    ownUserAvatarImage.isVisible = false
                    ownUserName.isVisible = false
                    ownUserOnlineStatus.isVisible = false
                    settingsButton.isVisible = false
                }

                is OwnProfileState.Loading -> {
                    shimmerContainer.isVisible = true
                    errorContainer.isVisible = false
                    ownUserAvatarImage.isVisible = false
                    ownUserName.isVisible = false
                    ownUserOnlineStatus.isVisible = false
                    settingsButton.isVisible = false
                    shimmerContainer.startShimmer()
                }
            }
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            settingsButton.setOnClickListener {
                findNavController().navigate(
                    OwnProfileFragmentDirections.actionOwnProfileFragmentToOwnSettingsFragment()
                )
            }
            errorComponent.retryButton.setOnClickListener {
                store.accept(OwnProfileEvent.Ui.ReloadData)
            }
        }
    }
}