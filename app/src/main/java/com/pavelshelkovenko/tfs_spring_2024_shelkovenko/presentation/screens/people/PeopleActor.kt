package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.domain.repository.UserRepository
import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.utils.runCatchingNonCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class PeopleActor(
    private val repository: UserRepository
): Actor<PeopleCommand, PeopleEvent>() {
    override fun execute(command: PeopleCommand): Flow<PeopleEvent> {
        return when(command) {
            is PeopleCommand.LoadData -> flow {
                runCatchingNonCancellation {
                    repository.getAllUsers()
                }.onSuccess { users ->
                    emit(PeopleEvent.Internal.DataLoaded(users = users))
                }.onFailure { error ->
                    emit(PeopleEvent.Internal.Error(throwable = error))
                }
            }

            is PeopleCommand.ProcessSearch -> flow {
                runCatchingNonCancellation {
                    repository.searchUsers(command.query)
                }.onSuccess { users ->
                    emit(PeopleEvent.Internal.DataLoaded(users = users))
                }.onFailure { error ->
                    emit(PeopleEvent.Internal.Error(throwable = error))
                }
            }
        }
    }

}