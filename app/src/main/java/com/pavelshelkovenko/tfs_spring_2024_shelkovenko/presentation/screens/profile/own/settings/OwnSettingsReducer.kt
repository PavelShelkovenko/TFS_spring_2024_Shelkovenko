package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.settings

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.models.AccountSettings
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.NoAction
import kotlinx.coroutines.flow.MutableStateFlow
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class OwnSettingsReducer : ScreenDslReducer<
        OwnSettingsEvent,
        OwnSettingsEvent.Ui,
        OwnSettingsEvent.Internal,
        OwnSettingsState,
        OwnSettingsEffect,
        OwnSettingsCommand>
    (OwnSettingsEvent.Ui::class, OwnSettingsEvent.Internal::class) {

    private val accountSettings = MutableStateFlow(AccountSettings())

    override fun Result.internal(event: OwnSettingsEvent.Internal) = when(event) {

        is OwnSettingsEvent.Internal.AccountSettingLoadedSuccessfully -> {
            accountSettings.value = event.accountSettings
            state {
                OwnSettingsState.Content(
                    accountSettings = accountSettings.value,
                    isInEditingMode = false
                )
            }
        }

        is OwnSettingsEvent.Internal.NameChangedSuccessfully -> {
            accountSettings.value = accountSettings.value.copy(
                userName = event.newName
            )
            state {
                OwnSettingsState.Content(
                    accountSettings = accountSettings.value,
                    isInEditingMode = false
                )
            }
        }

        is OwnSettingsEvent.Internal.EmailVisibilityChangedSuccessfully -> {
            accountSettings.value = accountSettings.value.copy(
                emailVisibility = event.newEmailVisibility
            )
            state {
                OwnSettingsState.Content(
                    accountSettings = accountSettings.value,
                    isInEditingMode = false
                )
            }
        }

        is OwnSettingsEvent.Internal.InvisibleModeChangedSuccessfully -> {
            val newAccountSettings = accountSettings.value.copy(
                isInvisibleMode = !event.newInvisibleModeState
            )
            accountSettings.value = newAccountSettings
            state {
                OwnSettingsState.Content(
                    accountSettings = newAccountSettings,
                    isInEditingMode = false
                )
            }
        }

        is OwnSettingsEvent.Internal.Error -> {
            state { OwnSettingsState.Error(event.errorMessageId) }
        }
        is OwnSettingsEvent.Internal.MinorError -> {
            effects { +OwnSettingsEffect.MinorError(event.errorMessageId) }
        }
    }

    override fun Result.ui(event: OwnSettingsEvent.Ui) = when(event) {

        is OwnSettingsEvent.Ui.StartProcess -> {
            commands { +OwnSettingsCommand.LoadAccountSettings }
        }

        is OwnSettingsEvent.Ui.ChangeName -> {
            commands { +OwnSettingsCommand.ChangeName(event.newName) }
        }

        is OwnSettingsEvent.Ui.ChangeEmailVisibility -> {
            if (event.newEmailVisibility == accountSettings.value.emailVisibility) {
                NoAction
            } else {
                commands { +OwnSettingsCommand.ChangeEmailVisibility(event.newEmailVisibility) }
            }
        }
        is OwnSettingsEvent.Ui.ChangeInvisibleMode -> {
            commands { +OwnSettingsCommand.ChangeInvisibleMode(event.newInvisibleModeState) }
        }

        is OwnSettingsEvent.Ui.ActivateEditingMode -> {
            state {
                OwnSettingsState.Content(
                    accountSettings = accountSettings.value,
                    isInEditingMode = true
                )
            }
        }

        OwnSettingsEvent.Ui.ReloadData -> {
            commands { +OwnSettingsCommand.LoadAccountSettings }
        }
    }
}