package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.another

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.interactors.GetAnotherProfileInteractor
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.runCatchingNonCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class AnotherProfileActor(
    private val getAnotherProfileInteractor: GetAnotherProfileInteractor
) : Actor<AnotherProfileCommand, AnotherProfileEvent>() {

    override fun execute(command: AnotherProfileCommand): Flow<AnotherProfileEvent> {
        return when (command) {
            is AnotherProfileCommand.LoadDataFromNetwork -> flow {
                runCatchingNonCancellation {
                    getAnotherProfileInteractor.getAnotherUserFromNetwork(userId = command.userId)
                }.onSuccess { ownUser ->
                    emit(AnotherProfileEvent.Internal.DataLoadedFromNetwork(user = ownUser))
                }.onFailure {
                    emit(AnotherProfileEvent.Internal.Error(errorMessageId = R.string.some_error_occurred))
                }
            }

            is AnotherProfileCommand.LoadDataFromCache -> flow {
                runCatchingNonCancellation {
                    getAnotherProfileInteractor.getAnotherUserFromCache(userId = command.userId)
                }.onSuccess { ownUser ->
                    emit(AnotherProfileEvent.Internal.DataLoadedFromCache(user = ownUser, userId = command.userId))
                }.onFailure {
                    emit(AnotherProfileEvent.Internal.ErrorLoadingFromCache(userId = command.userId))
                }
            }
        }
    }
}