package com.pavelshelkovenko.tfs_spring_2024_shelkovenko.presentation.screens.profile.another


import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class AnotherProfileReducer : ScreenDslReducer<
        AnotherProfileEvent,
        AnotherProfileEvent.Ui,
        AnotherProfileEvent.Internal,
        AnotherProfileState,
        AnotherProfileEffect,
        AnotherProfileCommand>
    (AnotherProfileEvent.Ui::class, AnotherProfileEvent.Internal::class) {

    override fun Result.internal(event: AnotherProfileEvent.Internal) = when (event) {
        is AnotherProfileEvent.Internal.DataLoadedFromNetwork -> state {
            AnotherProfileState.Content(anotherUser = event.user)
        }

        is AnotherProfileEvent.Internal.Error -> {
            if (state is AnotherProfileState.Content) {
                effects {
                    +AnotherProfileEffect.MinorError(errorMessageId = event.errorMessageId)
                }
            } else {
                state { AnotherProfileState.Error(errorMessageId =  event.errorMessageId) }
            }
        }

        is AnotherProfileEvent.Internal.DataLoadedFromCache -> {
            if (event.user == null) {
                state { AnotherProfileState.Loading }
            } else {
                state { AnotherProfileState.Content(anotherUser = event.user) }
            }
            commands { +AnotherProfileCommand.LoadDataFromNetwork(userId = event.userId) }
        }

        is AnotherProfileEvent.Internal.ErrorLoadingFromCache -> {
            commands { +AnotherProfileCommand.LoadDataFromNetwork(userId = event.userId) }
        }
    }

    override fun Result.ui(event: AnotherProfileEvent.Ui) = when (event) {

        is AnotherProfileEvent.Ui.StartProcess -> {
            commands { +AnotherProfileCommand.LoadDataFromCache(userId = event.userId) }

        }

        is AnotherProfileEvent.Ui.ReloadData -> {
            state { AnotherProfileState.Loading }
            commands { +AnotherProfileCommand.LoadDataFromCache(userId = event.userId) }
        }
    }
}