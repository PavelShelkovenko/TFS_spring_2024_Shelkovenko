package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.people


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

        is PeopleEvent.Internal.DataLoaded -> state {
            PeopleState.Content(userList = event.users)
        }
        is PeopleEvent.Internal.Error -> state {
            PeopleState.Error(errorMessage = event.throwable.message.toString())
        }
    }

    override fun Result.ui(event: PeopleEvent.Ui) = when(event) {

        is PeopleEvent.Ui.Init -> {
            state { PeopleState.Loading }
            commands { +PeopleCommand.LoadData }
        }

        is PeopleEvent.Ui.ReloadData -> {
            state { PeopleState.Loading }
            commands { +PeopleCommand.LoadData }
        }

        is PeopleEvent.Ui.QueryChanged -> {
            state { PeopleState.Loading }
            commands { +PeopleCommand.ProcessSearch(event.newQuery) }
        }
    }

}