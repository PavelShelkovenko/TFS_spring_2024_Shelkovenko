package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.own

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.interactors.GetOwnProfileInteractor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.runCatchingNonCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class OwnProfileActor(
    private val getOwnProfileInteractor: GetOwnProfileInteractor
): Actor<OwnProfileCommand, OwnProfileEvent>() {

    override fun execute(command: OwnProfileCommand): Flow<OwnProfileEvent> {
        return when(command) {
            is OwnProfileCommand.LoadDataFromNetwork -> flow {
                runCatchingNonCancellation {
                    getOwnProfileInteractor.getOwnProfileFromNetwork()
                }.onSuccess { ownUser ->
                    emit(OwnProfileEvent.Internal.DataLoadedFromNetwork(user = ownUser))
                }.onFailure {
                    emit(OwnProfileEvent.Internal.Error(errorMessageId = R.string.some_error_occurred))
                }
            }

            OwnProfileCommand.LoadDataFromCache -> flow {
                runCatchingNonCancellation {
                    getOwnProfileInteractor.getOwnProfileFromCache()
                }.onSuccess { ownUser ->
                    emit(OwnProfileEvent.Internal.DataLoadedFromCache(user = ownUser))
                }.onFailure {
                    emit(OwnProfileEvent.Internal.Error(errorMessageId = R.string.some_error_occurred))
                }
            }
        }
    }
}