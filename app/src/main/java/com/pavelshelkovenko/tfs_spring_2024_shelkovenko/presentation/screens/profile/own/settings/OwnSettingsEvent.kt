package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.settings

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.AccountSettings
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.EmailVisibility

sealed interface OwnSettingsEvent {

    sealed interface Ui: OwnSettingsEvent {

        data object StartProcess : Ui

        data class ChangeName(val newName: String): Ui

        data class ChangeInvisibleMode(val newInvisibleModeState: Boolean): Ui

        data class ChangeEmailVisibility(val newEmailVisibility: EmailVisibility): Ui

        data object ActivateEditingMode: Ui

        data object ReloadData: Ui
    }

    sealed interface Internal: OwnSettingsEvent {

        data class NameChangedSuccessfully(val newName: String): Internal

        data class EmailVisibilityChangedSuccessfully(val newEmailVisibility: EmailVisibility): Internal

        data class InvisibleModeChangedSuccessfully(val newInvisibleModeState: Boolean): Internal

        data class AccountSettingLoadedSuccessfully(val accountSettings: AccountSettings): Internal

        data class Error(val errorMessageId: Int): Internal

        data class MinorError(val errorMessageId: Int): Internal
    }
}