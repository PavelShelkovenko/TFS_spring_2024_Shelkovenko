package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.people


import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class PeopleReducer : ScreenDslReducer<
        PeopleEvent,
        PeopleEvent.Ui,
        PeopleEvent.Internal,
        PeopleState,
        PeopleEffect,
        PeopleCommand>
    (PeopleEvent.Ui::class, PeopleEvent.Internal::class) {

    override fun Result.internal(event: PeopleEvent.Internal) = when(event) {

        is PeopleEvent.Internal.DataLoadedFromNetwork -> state {
            PeopleState.Content(userList = event.users)
        }
        is PeopleEvent.Internal.Error -> state {
            PeopleState.Error(errorMessage = event.errorMessage)
        }

        is PeopleEvent.Internal.DataLoadedFromCache -> {
            if (event.users.isEmpty()) {
                state { PeopleState.Loading }
            } else {
                state { PeopleState.Content(userList = event.users) }
            }
        }
        is PeopleEvent.Internal.MinorError -> {
            effects { +PeopleEffect.MinorError(event.errorMessageId) }
        }
    }

    override fun Result.ui(event: PeopleEvent.Ui) = when(event) {

        is PeopleEvent.Ui.StartProcess -> {
            commands { +PeopleCommand.LoadDataFromCache }
            commands { +PeopleCommand.LoadDataFromNetwork }
        }

        is PeopleEvent.Ui.ReloadData -> {
            state { PeopleState.Loading }
            commands { +PeopleCommand.LoadDataFromNetwork }
        }

        is PeopleEvent.Ui.QueryChanged -> {
            state { PeopleState.Loading }
            commands { +PeopleCommand.ProcessSearch(event.newQuery) }
        }
    }

}