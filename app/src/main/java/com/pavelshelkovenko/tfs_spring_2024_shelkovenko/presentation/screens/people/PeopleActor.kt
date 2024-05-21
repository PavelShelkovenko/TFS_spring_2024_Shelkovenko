package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people

import com.pavelshelkovenko.tfs_spring_2024_shelkovenko.R
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
            is PeopleCommand.LoadDataFromNetwork -> flow {
                runCatchingNonCancellation {
                    repository.getAllUsersFromNetwork()
                }.onSuccess { users ->
                    emit(PeopleEvent.Internal.DataLoadedFromNetwork(users = users))
                }.onFailure {
                    emit(PeopleEvent.Internal.Error(errorMessageId = R.string.some_error_occurred))
                }
            }

            is PeopleCommand.LoadDataFromCache -> flow {
                runCatchingNonCancellation {
                    repository.getAllUsersFromCache()
                }.onSuccess { users ->
                    emit(PeopleEvent.Internal.DataLoadedFromCache(users = users))
                }.onFailure {
                    emit(PeopleEvent.Internal.Error(errorMessageId = R.string.some_error_occurred))
                }
            }

            is PeopleCommand.ProcessSearch -> flow {
                runCatchingNonCancellation {
                    repository.searchUsers(command.query)
                }.onSuccess { users ->
                    emit(PeopleEvent.Internal.DataLoadedFromNetwork(users = users))
                }.onFailure {
                    emit(PeopleEvent.Internal.SearchError(errorMessageId = R.string.search_error))
                }
            }


        }
    }

}