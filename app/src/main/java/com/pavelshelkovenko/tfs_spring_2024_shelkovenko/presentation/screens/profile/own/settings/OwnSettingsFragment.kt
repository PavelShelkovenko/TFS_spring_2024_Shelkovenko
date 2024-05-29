package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.databinding.FragmentOwnSettingsBinding
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.EmailVisibility
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.base.ElmBaseFragment
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.getApplication
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.makeOnlyFirstChatCapitalize
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.showErrorToast
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class OwnSettingsFragment :
    ElmBaseFragment<OwnSettingsEffect, OwnSettingsState, OwnSettingsEvent>(R.layout.fragment_own_settings) {

    private val binding: FragmentOwnSettingsBinding by viewBinding(FragmentOwnSettingsBinding::bind)

    @Inject
    lateinit var ownSettingsStoreFactory: OwnSettingsStoreFactory

    override fun onAttach(context: Context) {
        context.getApplication
            .appComponent
            .ownSettingsComponent()
            .inject(this)
        super.onAttach(context)
    }

    override val store: Store<OwnSettingsEvent, OwnSettingsEffect, OwnSettingsState> by elmStoreWithRenderer(
        elmRenderer = this
    ) {
        ownSettingsStoreFactory.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            store.accept(OwnSettingsEvent.Ui.StartProcess)
        }
        setupClickListeners()
        settingSpinnerAdapter()
    }


    private fun setupClickListeners() {
        with(binding) {
            startToEditUsernameButton.setOnClickListener {
                store.accept(OwnSettingsEvent.Ui.ActivateEditingMode)
            }
            saveUsernameButton.setOnClickListener {
                store.accept(
                    OwnSettingsEvent.Ui.ChangeName(
                        ownUserNameEditText.text.trim().toString()
                    )
                )
            }
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
            invisibleModeCheckbox.setOnClickListener {
                store.accept(
                    OwnSettingsEvent.Ui.ChangeInvisibleMode(
                        newInvisibleModeState = invisibleModeCheckbox.isChecked
                    )
                )
            }
            errorComponent.retryButton.setOnClickListener {
                store.accept(OwnSettingsEvent.Ui.ReloadData)
            }
        }
    }


    override fun handleEffect(effect: OwnSettingsEffect) {
        when (effect) {
            is OwnSettingsEffect.MinorError -> {
                showErrorToast(effect.errorMessageId, requireActivity())
            }
        }
    }


    override fun render(state: OwnSettingsState) {
        with(binding) {
            when (state) {
                is OwnSettingsState.Initial -> {
                    ownUserEmail.isVisible = false
                    ownUserAvatarImage.isVisible = false
                    errorContainer.isVisible = false
                    editNameHolder.isVisible = false
                    emailVisibilityText.isVisible = false
                    invisibleModeText.isVisible = false
                    emailVisibilitySpinner.isVisible = false
                    invisibleModeCheckbox.isVisible = false
                }

                is OwnSettingsState.Content -> {
                    ownUserEmail.isVisible = true
                    ownUserAvatarImage.isVisible = true
                    editNameHolder.isVisible = true
                    emailVisibilityText.isVisible = true
                    invisibleModeText.isVisible = true
                    emailVisibilitySpinner.isVisible = true
                    invisibleModeCheckbox.isVisible = true
                    ownUserEmail.text = state.accountSettings.email
                    emailVisibilitySpinner.prompt =
                        (state.accountSettings.emailVisibility.name.makeOnlyFirstChatCapitalize())
                    invisibleModeCheckbox.isChecked = !state.accountSettings.isInvisibleMode
                    Glide.with(root).load(state.accountSettings.avatarUrl).into(ownUserAvatarImage)
                    if (state.isInEditingMode) {
                        editNameContainer.isVisible = true
                        noneEditNameContainer.isVisible = false
                        ownUserNameEditText.setText(state.accountSettings.userName)
                    } else {
                        noneEditNameContainer.isVisible = true
                        editNameContainer.isVisible = false
                        ownUserNameText.text = state.accountSettings.userName
                    }
                    errorContainer.isVisible = false
                }

                is OwnSettingsState.Error -> {
                    errorContainer.isVisible = true
                    errorComponent.errorMessage.text = resources.getString(state.errorMessageId)
                    ownUserAvatarImage.isVisible = false
                    ownUserEmail.isVisible = false
                    editNameHolder.isVisible = false
                    emailVisibilityText.isVisible = false
                    invisibleModeText.isVisible = false
                    emailVisibilitySpinner.isVisible = false
                    invisibleModeCheckbox.isVisible = false
                }
            }
        }
    }

    private fun settingSpinnerAdapter() {
        val spinnerContent = EmailVisibility.entries.map { it.name.makeOnlyFirstChatCapitalize() }
        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.dropdown_email_visibility_item,
            spinnerContent
        )
        binding.emailVisibilitySpinner.adapter = adapter
        binding.emailVisibilitySpinner.setBackgroundResource(R.color.gray)
        binding.emailVisibilitySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val chosenValue = parent?.getItemAtPosition(position).toString().uppercase()
                    store.accept(
                        OwnSettingsEvent.Ui.ChangeEmailVisibility(
                            newEmailVisibility = EmailVisibility.valueOf(chosenValue)
                        )
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }
}