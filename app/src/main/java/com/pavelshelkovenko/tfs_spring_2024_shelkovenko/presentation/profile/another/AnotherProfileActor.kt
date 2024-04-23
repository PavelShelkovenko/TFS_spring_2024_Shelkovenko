package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.profile.another

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.usecase.GetAnotherProfileUseCase
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.runCatchingNonCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class AnotherProfileActor(
    private val getAnotherProfileUseCase: GetAnotherProfileUseCase
): Actor<AnotherProfileCommand, AnotherProfileEvent>() {

    override fun execute(command: AnotherProfileCommand): Flow<AnotherProfileEvent> {
        return when(command) {
            is AnotherProfileCommand.LoadData -> flow {
                runCatchingNonCancellation {
                    getAnotherProfileUseCase.invoke(userId = command.userId)
                }.onSuccess { ownUser ->
                    emit(AnotherProfileEvent.Internal.DataLoaded(user = ownUser))
                }.onFailure { error ->
                    emit(AnotherProfileEvent.Internal.Error(throwable = error))
                }
            }
        }
    }
}