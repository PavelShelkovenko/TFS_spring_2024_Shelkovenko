package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.own

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetOwnProfileUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.runCatchingNonCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class OwnProfileActor(
    private val getOwnProfileUseCase: GetOwnProfileUseCase
): Actor<OwnProfileCommand, OwnProfileEvent>() {

    override fun execute(command: OwnProfileCommand): Flow<OwnProfileEvent> {
        return when(command) {
            is OwnProfileCommand.LoadData -> flow {
                runCatchingNonCancellation {
                    getOwnProfileUseCase.invoke()
                }.onSuccess { ownUser ->
                    emit(OwnProfileEvent.Internal.DataLoaded(user = ownUser))
                }.onFailure { error ->
                    emit(OwnProfileEvent.Internal.Error(throwable = error))
                }
            }
        }
    }
}