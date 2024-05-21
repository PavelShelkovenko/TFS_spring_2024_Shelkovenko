package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.another

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentAnotherProfileBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.ElmBaseFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.getApplication
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.setColoredTextStatus
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.showErrorToast
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class AnotherProfileFragment :
    ElmBaseFragment<AnotherProfileEffect, AnotherProfileState, AnotherProfileEvent>(R.layout.fragment_another_profile) {

    private val binding: FragmentAnotherProfileBinding by viewBinding(FragmentAnotherProfileBinding::bind)

    private val args by navArgs<AnotherProfileFragmentArgs>()

    @Inject
    lateinit var anotherProfileStoreFactory: AnotherProfileStoreFactory

    override fun onAttach(context: Context) {
        context.getApplication
            .appComponent
            .anotherProfileComponent()
            .inject(this)
        super.onAttach(context)
    }

    override val store: Store<AnotherProfileEvent, AnotherProfileEffect, AnotherProfileState> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        anotherProfileStoreFactory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            store.accept(AnotherProfileEvent.Ui.StartProcess(args.userId))
        }
        setupClickListeners()
    }

    override fun handleEffect(effect: AnotherProfileEffect) {
        when(effect) {
            is AnotherProfileEffect.MinorError -> {
                showErrorToast(effect.errorMessageId, requireActivity())
            }
        }
    }

    override fun render(state: AnotherProfileState) {
        with(binding) {
            when (state) {
                is AnotherProfileState.Content -> {
                    shimmerContainer.isVisible = false
                    errorContainer.isVisible = false
                    userAvatarImage.isVisible = true
                    userName.isVisible = true
                    userOnlineStatus.isVisible = true
                    shimmerContainer.stopShimmer()
                    userName.text = state.anotherUser.name
                    userOnlineStatus.setColoredTextStatus(status = state.anotherUser.onlineStatus)
                    Glide.with(root).load(state.anotherUser.avatarUrl).into(userAvatarImage)
                }

                is AnotherProfileState.Error -> {
                    errorContainer.isVisible = true
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                    errorComponent.errorMessage.text = resources.getText(state.errorMessageId)
                    shimmerContainer.stopShimmer()
                }

                is AnotherProfileState.Initial -> {
                    errorContainer.isVisible = false
                    shimmerContainer.isVisible = false
                    userAvatarImage.isVisible = false
                    userName.isVisible = false
                    userOnlineStatus.isVisible = false
                }

                is AnotherProfileState.Loading -> {
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

    private fun setupClickListeners() {
        with(binding) {
            errorComponent.retryButton.setOnClickListener {
                store.accept(AnotherProfileEvent.Ui.ReloadData(args.userId))
            }
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}