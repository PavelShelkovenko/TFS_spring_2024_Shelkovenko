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
        is AnotherProfileEvent.Internal.DataLoaded -> state {
            AnotherProfileState.Content(anotherUser = event.user)
        }

        is AnotherProfileEvent.Internal.Error -> state {
            AnotherProfileState.Error(errorMessage = event.throwable.message.toString())
        }
    }

    override fun Result.ui(event: AnotherProfileEvent.Ui) = when (event) {

        is AnotherProfileEvent.Ui.StartProcess -> {
            state { AnotherProfileState.Loading }
            commands { +AnotherProfileCommand.LoadData(event.userId) }
        }

        is AnotherProfileEvent.Ui.ReloadData -> {
            state { AnotherProfileState.Loading }
            commands { +AnotherProfileCommand.LoadData(event.userId) }
        }
    }
}