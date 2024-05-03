package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentOwnProfileBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.ElmBaseFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.getApplication
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.setColoredTextStatus
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class OwnProfileFragment :
    ElmBaseFragment<OwnProfileEffect, OwnProfileState, OwnProfileEvent>(R.layout.fragment_own_profile) {

    private val binding: FragmentOwnProfileBinding by viewBinding(FragmentOwnProfileBinding::bind)

    @Inject
    lateinit var ownProfileStoreFactory: OwnProfileStoreFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.getApplication
            .appComponent
            .ownProfileComponent()
            .inject(this)
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

        binding.errorComponent.retryButton.setOnClickListener {
            store.accept(OwnProfileEvent.Ui.ReloadData)
        }
    }


    override fun render(state: OwnProfileState) {
        with(binding) {
            when (state) {
                is OwnProfileState.Content -> {
                    shimmerContainer.isVisible = false
                    errorContainer.isVisible = false
                    userAvatarImage.isVisible = true
                    userName.isVisible = true
                    userOnlineStatus.isVisible = true
                    shimmerContainer.stopShimmer()
                    userName.text = state.ownUser.name
                    userOnlineStatus.setColoredTextStatus(status = state.ownUser.onlineStatus)
                    Glide.with(root).load(state.ownUser.avatarUrl).into(userAvatarImage)
                }

                is OwnProfileState.Error -> {
                    errorContainer.isVisible = true
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                    shimmerContainer.stopShimmer()
                }

                is OwnProfileState.Initial -> {
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                }

                is OwnProfileState.Loading -> {
                    shimmerContainer.isVisible = true
                    errorContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                    shimmerContainer.startShimmer()
                }
            }
        }
    }
}