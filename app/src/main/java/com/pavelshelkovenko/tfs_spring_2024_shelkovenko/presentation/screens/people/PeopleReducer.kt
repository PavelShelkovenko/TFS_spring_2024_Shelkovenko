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
        is PeopleEvent.Internal.Error -> {
            if (state is PeopleState.Content) {
                effects { +PeopleEffect.MinorError(errorMessageId = event.errorMessageId) }
            } else {
                state { PeopleState.Error(errorMessageId = event.errorMessageId) }
            }
        }
        is PeopleEvent.Internal.DataLoadedFromCache -> {
            if (event.users.isEmpty()) {
                state { PeopleState.Loading }
            } else {
                state { PeopleState.Content(userList = event.users) }
            }
            commands { +PeopleCommand.LoadDataFromNetwork }
        }

        is PeopleEvent.Internal.MinorError -> {
            effects { +PeopleEffect.MinorError(errorMessageId = event.errorMessageId) }
        }

        is PeopleEvent.Internal.SearchError -> {
            state { PeopleState.Error(errorMessageId = event.errorMessageId) }
        }

        is PeopleEvent.Internal.ErrorLoadingFromCache -> {
            commands { +PeopleCommand.LoadDataFromNetwork }
        }
    }

    override fun Result.ui(event: PeopleEvent.Ui) = when(event) {

        is PeopleEvent.Ui.StartProcess -> {
            commands { +PeopleCommand.LoadDataFromCache }
        }

        is PeopleEvent.Ui.ReloadData -> {
            state { PeopleState.Loading }
            if (event.currentQuery.isEmpty()) {
                commands { +PeopleCommand.LoadDataFromCache }
            } else {
                commands { +PeopleCommand.ProcessSearch(event.currentQuery) }
            }
        }

        is PeopleEvent.Ui.QueryChanged -> {
            state { PeopleState.Loading }
            commands { +PeopleCommand.ProcessSearch(event.newQuery) }
        }
    }

}