package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own.settings

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.OwnSettingsRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.runCatchingNonCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class OwnSettingsActor(
    private val repository: OwnSettingsRepository,
) : Actor<OwnSettingsCommand, OwnSettingsEvent>() {
    override fun execute(command: OwnSettingsCommand): Flow<OwnSettingsEvent> {
        return when (command) {
            is OwnSettingsCommand.LoadAccountSettings -> flow {
                runCatchingNonCancellation {
                    repository.getAccountSetting()
                }.onSuccess { accountSettings ->
                    emit(
                        OwnSettingsEvent.Internal.AccountSettingLoadedSuccessfully(
                            accountSettings = accountSettings
                        )
                    )
                }.onFailure {
                    emit(OwnSettingsEvent.Internal.Error(R.string.loading_settings_error))
                }
            }

            is OwnSettingsCommand.ChangeName -> flow {
                runCatchingNonCancellation {
                    repository.changeName(command.newName)
                }.onSuccess {
                    emit(
                        OwnSettingsEvent.Internal.NameChangedSuccessfully(command.newName)
                    )
                }.onFailure {
                    emit(OwnSettingsEvent.Internal.MinorError(R.string.changing_username_error))
                }
            }

            is OwnSettingsCommand.ChangeEmailVisibility -> flow {
                runCatchingNonCancellation {
                    repository.changeEmailVisibility(command.newEmailVisibility)
                }.onSuccess {
                    emit(
                        OwnSettingsEvent.Internal.EmailVisibilityChangedSuccessfully(
                            command.newEmailVisibility
                        )
                    )
                }.onFailure {
                    emit(OwnSettingsEvent.Internal.MinorError(R.string.changing_email_visibility_error))
                }
            }

            is OwnSettingsCommand.ChangeInvisibleMode -> flow {
                runCatchingNonCancellation {
                    repository.changeInvisibleMode(command.newInvisibleModeState)
                }.onSuccess {
                    emit(
                        OwnSettingsEvent.Internal.InvisibleModeChangedSuccessfully(
                            command.newInvisibleModeState
                        )
                    )
                }.onFailure {
                    emit(OwnSettingsEvent.Internal.MinorError(R.string.changing_availability_error))
                }
            }
        }
    }
}