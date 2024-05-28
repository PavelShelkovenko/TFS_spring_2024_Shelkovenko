package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.settings

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.EmailVisibility

sealed interface OwnSettingsCommand {

    data object LoadAccountSettings: OwnSettingsCommand

    data class ChangeName(val newName: String): OwnSettingsCommand

    data class ChangeInvisibleMode(val newInvisibleModeState: Boolean): OwnSettingsCommand

    data class ChangeEmailVisibility(val newEmailVisibility: EmailVisibility): OwnSettingsCommand
}