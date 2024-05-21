package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetOwnProfileUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.runCatchingNonCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class OwnProfileActor(
    private val getOwnProfileUseCase: GetOwnProfileUseCase
): Actor<OwnProfileCommand, OwnProfileEvent>() {

    override fun execute(command: OwnProfileCommand): Flow<OwnProfileEvent> {
        return when(command) {
            is OwnProfileCommand.LoadDataFromNetwork -> flow {
                runCatchingNonCancellation {
                    getOwnProfileUseCase.getOwnProfileFromNetwork()
                }.onSuccess { ownUser ->
                    emit(OwnProfileEvent.Internal.DataLoadedFromNetwork(user = ownUser))
                }.onFailure {
                    emit(OwnProfileEvent.Internal.Error(errorMessageId = R.string.some_error_occurred))
                }
            }

            OwnProfileCommand.LoadDataFromCache -> flow {
                runCatchingNonCancellation {
                    getOwnProfileUseCase.getOwnProfileFromCache()
                }.onSuccess { ownUser ->
                    emit(OwnProfileEvent.Internal.DataLoadedFromCache(user = ownUser))
                }.onFailure {
                    emit(OwnProfileEvent.Internal.Error(errorMessageId = R.string.some_error_occurred))
                }
            }
        }
    }
}