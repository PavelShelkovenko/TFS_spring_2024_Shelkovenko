package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.settings

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.AccountSettings

sealed interface OwnSettingsState {

    data object Initial : OwnSettingsState

    data class Error(val errorMessageId: Int) : OwnSettingsState

    data class Content(
        val accountSettings: AccountSettings,
        val isInEditingMode: Boolean
    ) : OwnSettingsState
}
